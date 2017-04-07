package com.walfud.flowimageloader.dna.gene;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.walfud.flowimageloader.dna.Dna;
import com.walfud.walle.network.NetworkUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by walfud on 2016/3/18.
 */
public class LoadGene extends Gene {
    public static final String TAG = "LoadGene";

    public Uri uri;

    public LoadGene(Uri uri) {
        this.uri = uri;
    }

    @Override
    public Observable<Bitmap> onInject(Dna dna) {
        return NetworkUtils.get(uri.toString())
                .observeOn(Schedulers.io())
                .map(bytes -> BitmapFactory.decodeByteArray(bytes, 0, bytes.length))
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable();
    }
}
