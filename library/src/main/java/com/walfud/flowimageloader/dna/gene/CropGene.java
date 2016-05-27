package com.walfud.flowimageloader.dna.gene;

import android.graphics.Bitmap;

import com.walfud.flowimageloader.dna.Dna;
import com.walfud.walle.graphic.BitmapTransformer;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by walfud on 2016/3/19.
 */
public class CropGene extends Gene {
    public int dstWidth;
    public int dstHeight;

    public CropGene(int dstWidth, int dstHeight) {
        this.dstWidth = dstWidth;
        this.dstHeight = dstHeight;
    }

    @Override
    public Observable<Bitmap> onInject(Dna dna) {
        return Observable.just(null)
                .observeOn(Schedulers.computation())
                .map(aVoid -> {
                    Bitmap src = dna.bitmapRef.get();
                    return new BitmapTransformer(src)
                            .crop(dstWidth, dstHeight)
                            .get();
                });
    }
}
