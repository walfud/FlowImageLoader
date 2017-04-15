package com.walfud.flowimageloader.dna;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.walfud.cache.Cache;
import com.walfud.flowimageloader.dna.action.Action;
import com.walfud.flowimageloader.dna.action.InvalidateAction;
import com.walfud.flowimageloader.dna.gene.Gene;
import com.walfud.walle.lang.StrongReference;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by walfud on 2016/3/18.
 */
public class Dna {
    public List<Gene> geneList = new ArrayList<>();
    public List<Gene> unAbsorbGeneList = new ArrayList<>();
    public StrongReference<Bitmap> bitmapRef = new StrongReference<>(null);
    private Observable<Object> observable = Observable.empty();
    private Disposable disposable;
    private Listener mListener;
    private Handler mHandler = new Handler();
    private Cache<Bitmap> mCache;
    private Observable<ImageView> mLifecycler;
    private Set<ImageView> mVirusList = new HashSet<>();

    public Dna(Cache<Bitmap> cache, Observable<ImageView> lifecycler) {
        mCache = cache;
        mLifecycler = lifecycler;
    }

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
                String cacheId = new Gson().toJson(allGeneDeque);
                Bitmap cachedBitmap = mCache.get(cacheId);
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
            observable = observable.concatWith(Observable.fromIterable(unCachedGeneList).concatMap(gene -> gene.inject(this)));

            // Do action
            observable = observable.concatWith(action.act(this));

            //
            unAbsorbGeneList = new ArrayList<>();
        }

        return this;
    }

    public void evolve() {
        observable.takeUntil(mLifecycler.filter(mVirusList::contains)).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;

                if (mListener != null) {
                    runOnUiThread(() -> mListener.onStart(Dna.this));
                }
            }

            @Override
            public void onNext(Object object) {

            }

            @Override
            public void onComplete() {
                if (mListener != null) {
                    runOnUiThread(() -> mListener.onFinish(null));
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mListener != null) {
                    runOnUiThread(() -> mListener.onFinish(e));
                }
            }
        });
    }

    /**
     * Oh no... Once if meet virus, the life will end -_-#
     */
    public void addVirus(ImageView virus) {
        mVirusList.add(virus);
    }

    public void eliminate() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    //
    private void runOnUiThread(Runnable runnable) {
        if (Looper.getMainLooper().isCurrentThread()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

    //
    public interface Listener {
        @UiThread
        void onStart(Dna dna);

        @UiThread
        void onFinish(Throwable err);
    }
}
