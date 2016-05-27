package com.walfud.flowimageloader.dna.gene;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.walfud.flowimageloader.dna.Dna;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by walfud on 2016/3/18.
 */
public abstract class Gene {
    public String name;

    public Gene() {
        this.name = getClass().getSimpleName();
    }

    public Observable<Void> inject(Dna dna) {
        return Observable.just(null)
                .map(aVoid -> log(new Gson().toJson(this)))
                .concatMap(aVoid -> onInject(dna))
                .observeOn(AndroidSchedulers.mainThread())
                .map(bitmap -> {
                    dna.geneList.add(this);
                    dna.bitmapRef.set(bitmap);

                    return null;
                });
    }

    protected abstract Observable<Bitmap> onInject(Dna dna);

    private Void log(String fmt, Object... args) {
        Log.d("FlowImageLoader", String.format(fmt, args));
        return null;
    }
}
