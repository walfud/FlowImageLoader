package com.walfud.flowimageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.gson.Gson;
import com.walfud.walle.algorithm.hash.HashUtils;
import com.walfud.walle.io.IoUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * Created by walfud on 2015/11/18.
 */
public class DiskCache extends Cache {

    public static final String TAG = "DiskCache";

    private static final int TIME_WRITE_BACK_MS = 5 * 1000;

    private Context mContext;
    /**
     * first: md5 of cache id. second: content size
     */
    private RegexLruCache<String> mLruCache = new RegexLruCache<String>(10 * 1024 * 1024) {
        @Override
        protected int sizeOf(String key, String value) {
            String filename = HashUtils.md5(value);
            return (int) new File(mCacheDir, filename).length();
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, String oldValue, String newValue) {
            if (oldValue.equals(newValue)) {
                return;
            }

            String filename = HashUtils.md5(oldValue);
            new File(mCacheDir, filename).delete();

            saveIndex();
        }
    };
    private File mCacheDir;
    final private File mCacheIndexFile;
    private Subscription mWriterSubscription;

    public DiskCache(Context context) {
        mContext = context;
        mCacheDir = new File(context.getCacheDir(), "FlowImageLoader");
        mCacheDir.mkdirs();
        mCacheIndexFile = new File(mCacheDir, "index.json");

        // Init `mLruCache`
        String indexContent = IoUtils.read(mCacheIndexFile);
        Set<String> index = new Gson().fromJson(indexContent, Set.class);
        if (index == null) {
            index = new HashSet<>();
        }
        for (String action : index) {
            mLruCache.put(action, action);
        }

        mWriterSubscription = Observable.empty().subscribe();
    }

    @Override
    public synchronized void set(String id, Bitmap bitmap) {
        // Bitmap -> File
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String filename = HashUtils.md5(id);
        File cacheFile = new File(mCacheDir, filename);
        IoUtils.output(cacheFile, byteArrayOutputStream.toByteArray());

        mLruCache.put(id, id);

        saveIndex();
    }

    @Override
    public synchronized Bitmap get(String id) {
        if (mLruCache.get(id) != null) {
            File cacheFile = new File(mCacheDir, HashUtils.md5(id));
            return BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
        }

        return null;
    }

    @Override
    public synchronized void invalidById(String id) {
        mLruCache.remove(id);
    }

    @Override
    public synchronized void invalid(Uri uri) {
        mLruCache.removeRegex("^.*" + uri.toString() + ".*$");
    }

    @Override
    public void clear() {
        mLruCache.evictAll();
    }

    // internal
    private void saveIndex() {
        mWriterSubscription.unsubscribe();
        mWriterSubscription = Observable.<Void>just(null)
                .delay(TIME_WRITE_BACK_MS, TimeUnit.MILLISECONDS)
                .map(aVoid -> {
                    String indexFileContent = new Gson().toJson(mLruCache.keySet());
                    IoUtils.write(mCacheIndexFile, indexFileContent);
                    return null;
                })
                .subscribe();
    }
}
