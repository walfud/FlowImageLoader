package com.walfud.flowimageloader.flower;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.walfud.flowimageloader.FlowImageLoader;
import com.walfud.flowimageloader.dna.action.IntoAction;
import com.walfud.walle.UriUtils;

/**
 * Created by walfud on 2015/11/28.
 */
public class Flower {

    private static final int DEFFER_TIMEOUT = 2000;
    protected static final int SHAPE_DEFAULT = 0x0;
    protected static final int SHAPE_CIRCLED = 0x10;
    protected static final int SHAPE_ROUNDED = 0x20;

    protected ImageView mImageView;

    /**
     * Defer to get widget bound
     */
    protected long mDeferTime = 0;

    /**
     * Stub image
     */
    protected Uri mUri;
    protected int mLoadingId = IntoAction.INVALID_LOADING_ID;
    protected int mFailId = IntoAction.INVALID_FAIL_ID;

    /**
     * For transformer
     */
    protected int mShape = SHAPE_DEFAULT;
    protected double mRadiusX;
    protected double mRadiusY;

    public Flower(ImageView imageView) {
        mImageView = imageView;
    }

    // Function
    public void open(String url) {
        open(Uri.parse(url));
    }

    public void open(Uri uri) {
        open(uri, mLoadingId, mFailId);
    }
    public void open(final Uri uri, @DrawableRes final int loadingId, @DrawableRes final int failId) {
        mUri = uri;
        mLoadingId = loadingId;
        mFailId = failId;

        int width = mImageView.getWidth();
        int height = mImageView.getHeight();
        if (width == 0 && height == 0) {
            // Defer
            boolean defer = false;

            // Judge if time is out
            long currTimeInMillis = System.currentTimeMillis();
            if (mDeferTime == 0) {
                mDeferTime = currTimeInMillis;
                defer = true;
            } else {
                if (currTimeInMillis - mDeferTime < DEFFER_TIMEOUT) {
                    defer = true;
                }
            }

            //
            if (defer) {
                new Handler().post(() -> open(uri, loadingId, failId));
            }
        } else {
            doOpen(mImageView, uri);
        }
    }

    // Internal
    /**
     * Display the image to widget. This time, `imageView` has been layout.
     *
     * @param imageView
     * @param uri
     */
    protected void doOpen(ImageView imageView, Uri uri) {
        if (UriUtils.isEmpty(uri)) {
            // Do nothing
        } else {
            FlowImageLoader flowImageLoader = FlowImageLoader.with(imageView.getContext()).load(uri);

            // Scale type
            switch (imageView.getScaleType()) {
                case FIT_XY:
                    flowImageLoader.resize(imageView.getWidth(), imageView.getHeight());
                    break;

                case CENTER_CROP:
                    flowImageLoader.crop(imageView.getWidth(), imageView.getHeight());
                    break;

                case CENTER_INSIDE:
                    flowImageLoader.centerInside(imageView.getWidth(), imageView.getHeight());
                    break;

                default:
                    break;
            }

            // Transformer
            switch (mShape) {
                case SHAPE_CIRCLED:
                    flowImageLoader.circle();
                    break;

                case SHAPE_ROUNDED:
                    flowImageLoader.round(mRadiusX, mRadiusY);
                    break;

                default:
                    break;
            }
            flowImageLoader.into(imageView, mLoadingId, mFailId).cache();

            flowImageLoader.pls();
        }
    }

    //
    public void die(Uri uri) {
//        FlowImageLoader.with(mImageView.getContext()).invalidate(uri);
    }
}
