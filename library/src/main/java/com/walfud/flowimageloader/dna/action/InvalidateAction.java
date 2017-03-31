package com.walfud.flowimageloader.dna.action;

import com.walfud.flowimageloader.cache.CacheManager;
import com.walfud.flowimageloader.dna.Dna;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by walfud on 2016/3/19.
 */
public class InvalidateAction extends Action {
    @Override
    public Observable<Object> onAct(Dna dna) {
        return Observable.<Object>just(0)
                .observeOn(Schedulers.io())
                .map(object -> {
                    String cacheId = CacheManager.toCacheId(dna.geneList);
                    CacheManager.getInstance().invalidate(cacheId);

                    return object;
                });
    }
}
