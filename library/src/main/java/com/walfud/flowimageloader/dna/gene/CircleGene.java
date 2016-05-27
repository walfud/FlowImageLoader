package com.walfud.flowimageloader.dna.gene;

import android.graphics.Bitmap;

import com.walfud.flowimageloader.dna.Dna;
import com.walfud.walle.graphic.BitmapTransformer;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by walfud on 2016/3/19.
 */
public class CircleGene extends Gene {
    @Override
    public Observable<Bitmap> onInject(Dna dna) {
        return Observable.just(null)
                .observeOn(Schedulers.computation())
                .map(aVoid -> {
                    Bitmap src = dna.bitmapRef.get();
                    return new BitmapTransformer(src)
                            .circle()
                            .get();
                });
    }
}
