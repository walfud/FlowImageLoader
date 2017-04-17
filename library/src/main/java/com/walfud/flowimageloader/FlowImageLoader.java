package com.walfud.flowimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.RxFragment;
import com.walfud.cache.BitmapCache;
import com.walfud.cache.Cache;
import com.walfud.flowimageloader.dna.Dna;
import com.walfud.flowimageloader.dna.action.CacheAction;
import com.walfud.flowimageloader.dna.action.IntoAction;
import com.walfud.flowimageloader.dna.action.InvalidateAction;
import com.walfud.flowimageloader.dna.gene.CenterInsideGene;
import com.walfud.flowimageloader.dna.gene.CircleGene;
import com.walfud.flowimageloader.dna.gene.CropGene;
import com.walfud.flowimageloader.dna.gene.Gene;
import com.walfud.flowimageloader.dna.gene.LoadGene;
import com.walfud.flowimageloader.dna.gene.ResizeGene;
import com.walfud.flowimageloader.dna.gene.RoundGene;
import com.walfud.walle.WallE;
import com.walfud.walle.algorithm.Comparator;
import com.walfud.walle.collection.CollectionUtils;

import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 * {@code
 * FlowImageLoader.with(getApplicationContext())
 *       .load(Uri.parse("https://raw.githubusercontent.com/walfud/lib-flowimageloader/master/doc/workflow.png"))
 *       .resize(1000, 500)
 *       .into(mIv)
 *       .cache()
 *       .pls();
 * }
 * </pre>
 * Created by walfud on 2015/11/11.
 */
public class FlowImageLoader {

    public static final String TAG = "FlowImageLoader";

    private static Cache<Bitmap> sCache;
    private static Subject<ImageView> sReusableViewLifecycler;
    private Dna mDna;
    /**
     * Whether `load` has been called.
     */
    private boolean mHasUri = false;

    private FlowImageLoader(Context context, ObservableTransformer activityOrFragmentLifecycle) {
        mDna = new Dna(sCache, activityOrFragmentLifecycle, sReusableViewLifecycler);
        mHasUri = false;
    }

    // Function

    /**
     * Activity lifecycle aware
     * @param rxActivity
     * @return
     */
    public static FlowImageLoader with(RxActivity rxActivity) {
        return with(rxActivity, rxActivity.bindToLifecycle());
    }

    /**
     * Fragment lifecycle aware
     * @param rxFragment
     * @return
     */
    public static FlowImageLoader with(RxFragment rxFragment) {
        return with(rxFragment.getContext(), rxFragment.bindToLifecycle());
    }

    /**
     * No activity/fragment lifecycle aware
     * @param context
     * @return
     */
    public static FlowImageLoader with(Context context) {
        return with(context, upstream -> upstream);
    }

    public FlowImageLoader load(String url) {
        return load(Uri.parse(url));
    }
    public FlowImageLoader load(Uri uri) {
        mDna.eat(new LoadGene(uri));
        mHasUri = true;

        return this;
    }

    /**
     * If there is uri in flow, `resize` means resize the local bitmap;
     * Otherwise, means send a resize request to server. But We can'first promise the server will
     * do correct resize action.
     *
     * @param width
     * @param height
     * @return
     * @see #mHasUri
     */
    public FlowImageLoader resize(int width, int height) {
        if (!mHasUri) {
            // 'resize' request to server
            // TODO:
            throw new RuntimeException("not impl");
        } else {
            // 'resize' action in local bitmap
            mDna.eat(new ResizeGene(width, height));
        }

        return this;
    }

    public FlowImageLoader circle() {
        mDna.eat(new CircleGene());

        return this;
    }

    public FlowImageLoader round(double radiusX, double radiusY) {
        mDna.eat(new RoundGene(radiusX, radiusY));

        return this;
    }

    public FlowImageLoader crop(int width, int height) {
        mDna.eat(new CropGene(width, height));

        return this;
    }

    public FlowImageLoader centerInside(int width, int height) {
        mDna.eat(new CenterInsideGene(width, height));

        return this;
    }

    public FlowImageLoader cache() {
        return cache(true, true);
    }

    public FlowImageLoader cache(boolean memory, boolean disk) {
        mDna.grow(new CacheAction(sCache));

        return this;
    }

    public FlowImageLoader invalidate() {
        return invalidate(true, true);
    }

    public FlowImageLoader invalidate(boolean memory, boolean disk) {
        mDna.grow(new InvalidateAction(sCache));

        return this;
    }

    public FlowImageLoader into(ImageView imageView) {
        return into(imageView, 0, 0);
    }

    public FlowImageLoader into(ImageView imageView, @DrawableRes int loadingId, @DrawableRes int failId) {
        mDna.grow(new IntoAction(imageView, loadingId, failId), new Dna.ActionListener() {
            @Override
            public void preAction(List<Gene> todoGeneList) {
                // Cancel old request
                sReusableViewLifecycler.onNext(imageView);
                mDna.addVirus(imageView);

                // Callback
                if (loadingId != 0 && CollectionUtils.find(todoGeneList, (Comparator<Void, Gene>) (a, b) -> b instanceof LoadGene ? 0 : -1) != null) {
                    imageView.setImageResource(loadingId);
                }
            }

            @Override
            public void postAction(Throwable throwable) {
                if (throwable != null && failId != 0) {
                    imageView.setImageResource(failId);
                }
            }
        });

        return this;
    }

    /**
     * Run the flow
     */
    public void pls() {
        mDna.evolve();
    }

    /**
     * Cancel the flow
     */
    public void cancel() {
        mDna.eliminate();
    }

    public Cache<Bitmap> getCache() {
        return sCache;
    }

    //
    private static FlowImageLoader with(Context context, ObservableTransformer activityOrFragmentLifecycle) {
        Context appContext = context.getApplicationContext();
        if (sCache == null) {
            // Initialization
            WallE.initialize(appContext);
            sCache = new BitmapCache(context, 100L * 1024 * 1024, 100L * 1024 * 1024);
            sReusableViewLifecycler = PublishSubject.create();
        }

        return new FlowImageLoader(appContext, activityOrFragmentLifecycle);
    }
}
