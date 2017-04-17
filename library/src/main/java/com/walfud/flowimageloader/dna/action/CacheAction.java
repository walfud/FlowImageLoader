package com.walfud.flowimageloader.dna.action;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.walfud.cache.Cache;
import com.walfud.flowimageloader.dna.Dna;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by walfud on 2016/3/19.
 */
public class CacheAction extends Action {

    private Cache<Bitmap> mCache;
    public CacheAction(Cache<Bitmap> cache) {
        mCache = cache;
    }

    @Override
    public Observable<Object> onAct(Dna dna) {
        return Observable.just(0)
                .observeOn(Schedulers.io())
                .map(object -> {
                    String cacheId = new Gson().toJson(dna.getGeneList());
                    Bitmap bitmap = dna.getBitmap();
                    mCache.add(cacheId, bitmap);

                    return object;
                });
    }
}
