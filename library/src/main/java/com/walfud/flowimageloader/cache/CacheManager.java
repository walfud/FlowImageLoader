package com.walfud.flowimageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;

/**
 * Created by walfud on 2015/11/15.
 */
public class CacheManager {

    public static final String TAG = "CacheManager";
    private static CacheManager sInstance;

    private Context mContext;
    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    private CacheManager() {
    }

    // Function
    public void init(Context context) {
        mContext = context;
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(mContext);
    }

    public Bitmap get(String cacheId) {
        return get(cacheId, true, true);
    }
    public Bitmap get(String cacheId, boolean memory, boolean disk) {
        Bitmap cachedBitmap = null;
        if (memory) {
            cachedBitmap = mMemoryCache.get(cacheId);
        }
        if (cachedBitmap == null && disk) {
            cachedBitmap = mDiskCache.get(cacheId);
        }

        return cachedBitmap;
    }

    public CacheManager set(String cacheId, Bitmap bitmap) {
        return set(cacheId, bitmap, true, true);
    }
    public CacheManager set(String cacheId, Bitmap bitmap, boolean memory, boolean disk) {
        if (memory) {
            mMemoryCache.set(cacheId, bitmap);
        }
        if (disk) {
            mDiskCache.set(cacheId, bitmap);
        }

        return this;
    }
    public CacheManager invalidate(String cacheId) {
        return invalidate(cacheId, true, true);
    }
    public CacheManager invalidate(String cacheId, boolean memory, boolean disk) {
        if (memory) {
            mMemoryCache.invalidById(cacheId);
        }
        if (disk) {
            mDiskCache.invalidById(cacheId);
        }

        return this;
    }

    // internal

    // Helper
    public static CacheManager getInstance() {
        if (sInstance == null) {
            sInstance = new CacheManager();
        }

        return sInstance;
    }

    public static String toCacheId(Object object) {
        return new Gson().toJson(object);
    }
}
