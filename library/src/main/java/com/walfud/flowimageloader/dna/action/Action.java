package com.walfud.flowimageloader.dna.action;

import android.util.Log;

import com.walfud.flowimageloader.dna.Dna;

import rx.Observable;

/**
 * Created by walfud on 2016/3/19.
 */
public abstract class Action {
    public String name;

    public Action() {
        this.name = getClass().getSimpleName();
    }

    public Observable<Void> act(Dna dna) {
        return Observable.just(null)
                .map(aVoid -> log(name))
                .concatMap(aVoid -> onAct(dna));
    }

    protected abstract Observable<Void> onAct(Dna dna);

    private Void log(String fmt, Object... args) {
        Log.d("FlowImageLoader", String.format(fmt, args));
        return null;
    }
}
