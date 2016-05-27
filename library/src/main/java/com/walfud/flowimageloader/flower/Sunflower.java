package com.walfud.flowimageloader.flower;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Created by walfud on 2015/11/28.
 */
public class Sunflower extends Flower {

    private static int sLoadingId;
    private static int sFailId;

    public Sunflower(ImageView imageView) {
        super(imageView);
        mLoadingId = sLoadingId;
        mFailId = sFailId;
        mShape = SHAPE_CIRCLED;
    }

    //
    public static void seed(@DrawableRes int loadingId, @DrawableRes int failId) {
        sLoadingId = loadingId;
        sFailId = failId;
    }
}
