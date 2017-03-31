package com.walfud.flowimageloader.dna.action;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.walfud.flowimageloader.dna.Dna;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

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
    public Observable<Object> onAct(Dna dna) {
        return Observable.<Object>just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .map(object -> {
                    Bitmap bitmap = dna.bitmapRef.get();

                    if (imageView != null && bitmap != null) {
                        // Success
                        imageView.setImageBitmap(bitmap);
                    }

                    return object;
                });
    }
}
