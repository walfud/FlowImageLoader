package com.walfud.flowimageloader.dna;

import android.graphics.Bitmap;
import android.os.Handler;

import com.walfud.flowimageloader.cache.CacheManager;
import com.walfud.flowimageloader.dna.action.Action;
import com.walfud.flowimageloader.dna.action.InvalidateAction;
import com.walfud.flowimageloader.dna.gene.Gene;
import com.walfud.walle.lang.StrongReference;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by walfud on 2016/3/18.
 */
public class Dna {
    public List<Gene> geneList = new ArrayList<>();
    public List<Gene> unAbsorbGeneList = new ArrayList<>();
    public StrongReference<Bitmap> bitmapRef = new StrongReference<>(null);
    private Observable<Void> observable = Observable.just(null);
    private Subscription subscription;
    private Listener mListener;

    public Dna digest(Gene gene) {
        unAbsorbGeneList.add(gene);
        return this;
    }

    public Dna absorb(Action action) {
        if (action instanceof InvalidateAction) {
            // `InvalidateAction` should not replay any gene
            geneList.addAll(unAbsorbGeneList);
            unAbsorbGeneList = new ArrayList<>();
            bitmapRef.set(null);
            observable = observable.concatWith(action.act(this));
        } else {
            // Query cache
            Deque<Gene> allGeneDeque = new ArrayDeque<>(geneList);
            allGeneDeque.addAll(unAbsorbGeneList);
            Deque<Gene> unCachedGeneList = new ArrayDeque<>();
            while (allGeneDeque.size() > geneList.size()) {
                String cacheId = CacheManager.toCacheId(allGeneDeque);
                Bitmap cachedBitmap = CacheManager.getInstance().get(cacheId);
                if (cachedBitmap == null) {
                    Gene unCachedGene = allGeneDeque.removeLast();
                    unCachedGeneList.addFirst(unCachedGene);
                } else {
                    // Hit
                    geneList.clear();
                    geneList.addAll(allGeneDeque);
                    bitmapRef.set(cachedBitmap);
                    break;
                }
            }

            // Replay the un-cached gene
            observable = observable.concatWith(Observable.from(unCachedGeneList).concatMap(gene -> gene.inject(this)));

            // Do action
            observable = observable.concatWith(action.act(this));

            //
            unAbsorbGeneList = new ArrayList<>();
        }

        return this;
    }

    public void evolve() {
        subscription = observable.subscribe(new Subscriber<Void>() {
            @Override
            public void onStart() {
                super.onStart();

                if (mListener != null) {
                    new Handler().post(() -> mListener.onStart());
                }
            }

            @Override
            public void onNext(Void aVoid) {

            }

            @Override
            public void onCompleted() {
                if (mListener != null) {
                    mListener.onFinish(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mListener != null) {
                    mListener.onFinish(false);
                }
            }
        });
    }

    public void eliminate() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    //
    public interface Listener {
        void onStart();
        void onFinish(boolean suc);
    }
}
