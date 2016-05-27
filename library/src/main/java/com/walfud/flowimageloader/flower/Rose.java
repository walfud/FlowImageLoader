package com.walfud.flowimageloader.flower;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.walfud.walle.DensityTransformer;
import com.walfud.walle.WallE;

/**
 * Created by walfud on 2015/11/28.
 */
public class Rose extends Flower {

    private static int sLoadingId;
    private static int sFailId;

    {
        if (WallE.getContext() == null) {
            WallE.initialize(mImageView.getContext());
        }
    }

    public Rose(ImageView imageView) {
        super(imageView);
        mLoadingId = sLoadingId;
        mFailId = sFailId;
        mShape = SHAPE_ROUNDED;
        mRadiusX = DensityTransformer.dp2px(6);
        mRadiusY = DensityTransformer.dp2px(6);
    }

    //
    public static void seed(@DrawableRes int loadingId, @DrawableRes int failId) {
        sLoadingId = loadingId;
        sFailId = failId;
    }
}
