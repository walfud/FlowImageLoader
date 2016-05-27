package com.walfud.flowimageloader.dna.action;

import android.graphics.Bitmap;

import com.walfud.flowimageloader.cache.CacheManager;
import com.walfud.flowimageloader.dna.Dna;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by walfud on 2016/3/19.
 */
public class CacheAction extends Action {
    @Override
    public Observable<Void> onAct(Dna dna) {
        return Observable.<Void>just(null)
                .observeOn(Schedulers.io())
                .map(aVoid -> {
                    String cacheId = CacheManager.toCacheId(dna.geneList);
                    Bitmap bitmap = dna.bitmapRef.get();
                    CacheManager cacheManager = CacheManager.getInstance();
                    cacheManager.set(cacheId, bitmap);

                    return null;
                });
    }
}
