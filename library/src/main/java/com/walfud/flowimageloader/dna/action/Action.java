package com.walfud.flowimageloader.dna.action;

import android.util.Log;

import com.walfud.flowimageloader.dna.Dna;
import com.walfud.flowimageloader.dna.gene.Gene;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by walfud on 2016/3/19.
 */
public abstract class Action {
    public String name;

    public Action() {
        this.name = getClass().getSimpleName();
    }

    public Observable<Object> act(Dna dna, List<Gene> todoGeneList) {
        return Observable.just(0)
                .map(object -> log(name))
                .concatMap(object -> onAct(dna, todoGeneList));
    }

    protected abstract Observable<Object> onAct(Dna dna, List<Gene> todoGeneList);

    private int log(String fmt, Object... args) {
        Log.v("FlowImageLoader", String.format(fmt, args));
        return 0;
    }
}
