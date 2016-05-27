package com.walfud.flowimageloader.cache;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by walfud on 2015/11/15.
 */
public abstract class Cache {

    public static final String TAG = "Cache";

    public abstract void set(String id, Bitmap bitmap);

    public abstract Bitmap get(String id);

    public abstract void invalidById(String id);

    /**
     * Remove all with the same uri.
     * @param uri
     */
    public abstract void invalid(Uri uri);

    public abstract void clear();
}
