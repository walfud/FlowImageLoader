package com.walfud.flowimageloader.flower;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.walfud.walle.WallE;
import com.walfud.walle.android.DensityUtils;

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
        mRadiusX = DensityUtils.dp2px(6);
        mRadiusY = DensityUtils.dp2px(6);
    }

    //
    public static void seed(@DrawableRes int loadingId, @DrawableRes int failId) {
        sLoadingId = loadingId;
        sFailId = failId;
    }
}
