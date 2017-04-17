package com.walfud.flowimageloader.dna.gene;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.walfud.flowimageloader.dna.Dna;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by walfud on 2016/3/18.
 */
public abstract class Gene {
    public String name;

    public Gene() {
        this.name = getClass().getSimpleName();
    }

    public Observable<Object> inject(Dna dna) {
        return Observable.just(0)
                .map(object -> log(new Gson().toJson(this)))
                .concatMap(object -> onInject(dna))
                .observeOn(AndroidSchedulers.mainThread())
                .map(bitmap -> {
                    dna.absorb(this, bitmap);

                    return 0;
                });
    }

    protected abstract Observable<Bitmap> onInject(Dna dna);

    private Object log(String fmt, Object... args) {
        Log.v("FlowImageLoader", String.format(fmt, args));
        return 0;
    }
}
