package com.walfud.flowimageloader.cache;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by walfud on 2015/11/15.
 */
public class MemoryCache extends Cache {

    public static final String TAG = "MemoryCache";

    /**
     * key: action list name, value: bitmap
     */
    private RegexLruCache<Bitmap> mLruCache = new RegexLruCache<Bitmap>(10 * 1024 * 1024) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getAllocationByteCount();
        }
    };

    @Override
    public void set(String id, Bitmap bitmap) {
        mLruCache.put(id, bitmap);
    }

    @Override
    public synchronized Bitmap get(String id) {
        return mLruCache.get(id);
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
}
