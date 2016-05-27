package com.walfud.flowimageloader.dna.action;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.walfud.flowimageloader.dna.Dna;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by walfud on 2016/3/19.
 */
public class IntoAction extends Action {
    public static final int INVALID_LOADING_ID = 0;
    public static final int INVALID_FAIL_ID = 0;

    public ImageView imageView;

    public IntoAction(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public Observable<Void> onAct(Dna dna) {
        return Observable.<Void>just(null)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aVoid -> {
                    Bitmap bitmap = dna.bitmapRef.get();

                    if (imageView != null && bitmap != null) {
                        // Success
                        imageView.setImageBitmap(bitmap);
                    }

                    return null;
                });
    }
}
