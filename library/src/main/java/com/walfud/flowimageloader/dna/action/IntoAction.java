package com.walfud.flowimageloader.dna.action;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

import com.walfud.flowimageloader.dna.Dna;
import com.walfud.flowimageloader.dna.gene.Gene;
import com.walfud.flowimageloader.dna.gene.LoadGene;
import com.walfud.walle.collection.CollectionUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by walfud on 2016/3/19.
 */
public class IntoAction extends Action {
    public static final String TAG = "IntoAction";

    public ImageView imageView;
    public int loadingId;
    public int failId;

    public IntoAction(ImageView imageView) {
        this(imageView, 0, 0);
    }

    public IntoAction(ImageView imageView, int loadingId, int failId) {
        this.imageView = imageView;
        this.loadingId = loadingId;
        this.failId = failId;
    }

    @Override
    public Observable<Object> onAct(Dna dna, List<Gene> todoGeneList) {
        return Observable.<Object>just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .map(object -> {
                    Bitmap bitmap = dna.getBitmap();

                    if (imageView != null && bitmap != null) {
                        // Success
                        if (CollectionUtils.find(todoGeneList, (CollectionUtils.Predicate<Gene>) gene -> gene instanceof LoadGene) != null) {
                            // Transition animation
                            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{new BitmapDrawable(imageView.getResources(), imageView.getDrawingCache()), new BitmapDrawable(imageView.getResources(), bitmap)});
                            transitionDrawable.setCrossFadeEnabled(true);
                            imageView.setImageDrawable(transitionDrawable);
                            transitionDrawable.startTransition(300);
                        } else {
                            // Hit cache, just draw bitmap
                            imageView.setImageBitmap(bitmap);
                        }
                    }

                    return object;
                });
    }
}
