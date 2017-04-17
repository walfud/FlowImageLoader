package com.walfud.flowimageloader.dna;

import android.graphics.Bitmap;
import android.support.annotation.UiThread;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.walfud.cache.Cache;
import com.walfud.flowimageloader.dna.action.Action;
import com.walfud.flowimageloader.dna.action.InvalidateAction;
import com.walfud.flowimageloader.dna.gene.Gene;
import com.walfud.walle.android.Etc;
import com.walfud.walle.lang.StrongReference;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by walfud on 2016/3/18.
 */
public class Dna {
    public static final String TAG = "Dna";

    private List<Gene> mGeneList = new ArrayList<>();
    private List<Gene> mUnAbsorbGeneList = new ArrayList<>();
    private StrongReference<Bitmap> mBitmapRef = new StrongReference<>(null);
    private Observable<Object> mObservable = Observable.empty();
    private Disposable mDisposable;
    private Listener mListener;
    private Cache<Bitmap> mCache;
    private ObservableTransformer mActivityOrFragmentLifecycle;
    private Observable<ImageView> mReusableViewLifecycler;
    private Set<ImageView> mVirusList = new HashSet<>();

    public Dna(Cache<Bitmap> cache, ObservableTransformer activityOrFragmentLifecycle, Observable<ImageView> lifecycler) {
        mCache = cache;
        mActivityOrFragmentLifecycle = activityOrFragmentLifecycle;
        mReusableViewLifecycler = lifecycler;
    }

    public Dna eat(Gene gene) {
        mUnAbsorbGeneList.add(gene);
        return this;
    }

    public Dna grow(Action action) {
        return grow(action, null);
    }

    public Dna grow(Action action, ActionListener actionListener) {
        if (action instanceof InvalidateAction) {
            // `InvalidateAction` should not replay any gene
            mGeneList.addAll(mUnAbsorbGeneList);
            mUnAbsorbGeneList.clear();
            mBitmapRef.set(null);
            mObservable = mObservable.concatWith(action.act(this));
        } else {
            // Query cache
            Deque<Gene> allGeneDeque = new ArrayDeque<>(mGeneList);
            allGeneDeque.addAll(mUnAbsorbGeneList);
            mUnAbsorbGeneList.clear();
            Deque<Gene> unCachedGeneList = new ArrayDeque<>();
            while (allGeneDeque.size() > mGeneList.size()) {
                String cacheId = new Gson().toJson(allGeneDeque);
                Bitmap cachedBitmap = mCache.get(cacheId);
                if (cachedBitmap == null) {
                    Gene unCachedGene = allGeneDeque.removeLast();
                    unCachedGeneList.addFirst(unCachedGene);
                } else {
                    // Hit
                    mGeneList.clear();
                    mGeneList.addAll(allGeneDeque);
                    mBitmapRef.set(cachedBitmap);
                    break;
                }
            }

            Observable observable = Observable
                    .fromIterable(unCachedGeneList).concatMap(gene -> gene.inject(this))  // Replay the un-absorbed gene
                    .concatWith(action.act(this))                                         // Do Action
                    .doOnSubscribe(disposable -> {
                        if (actionListener != null) {
                            Etc.runOnUiThread(args -> actionListener.preAction());
                        }
                    })
                    .doOnComplete(() -> {
                        if (actionListener != null) {
                            Etc.runOnUiThread(args -> actionListener.postAction(null));
                        }
                    })
                    .doOnError(throwable -> {
                        if (actionListener != null) {
                            Etc.runOnUiThread(args -> actionListener.postAction(throwable));
                        }
                    });

            // Do action
            mObservable = mObservable.concatWith(observable);
        }

        return this;
    }

    public void evolve() {
        mObservable
                .takeUntil(mReusableViewLifecycler.filter(mVirusList::contains))    // Re-usable view
                .compose(mActivityOrFragmentLifecycle)                              // Activity/Fragment
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;

                        if (mListener != null) {
                            Etc.runOnUiThread(args -> mListener.onStart(Dna.this));
                        }
                    }

                    @Override
                    public void onNext(Object object) {

                    }

                    @Override
                    public void onComplete() {
                        if (mListener != null) {
                            Etc.runOnUiThread(args -> mListener.onFinish(null));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mListener != null) {
                            Etc.runOnUiThread(args -> mListener.onFinish(e));
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
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    /**
     * When absorb, the `gene` takes effect
     * @param gene
     * @param bitmap
     */
    public void absorb(Gene gene, Bitmap bitmap) {
        mGeneList.add(gene);
        mBitmapRef.set(bitmap);
    }

    public List<Gene> getGeneList() {
        return mGeneList;
    }

    public Bitmap getBitmap() {
        return mBitmapRef.get();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    //
    public interface ActionListener {
        @UiThread
        void preAction();

        @UiThread
        void postAction(Throwable throwable);
    }

    public interface Listener {
        @UiThread
        void onStart(Dna dna);

        @UiThread
        void onFinish(Throwable err);
    }
}
