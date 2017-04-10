package com.walfud.flowimageloader.dna.gene;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.walfud.flowimageloader.dna.Dna;
import com.walfud.walle.network.NetworkUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by walfud on 2016/3/18.
 */
public class LoadGene extends Gene {
    public static final String TAG = "LoadGene";
    private static Map<Uri, Bitmap> sPool;

    static {
        sPool = new HashMap<>();
    }

    public Uri uri;

    public LoadGene(Uri uri) {
        this.uri = uri;
    }

    @Override
    public Observable<Bitmap> onInject(Dna dna) {
        if (!sPool.containsKey(uri)) {

            sPool.put(uri, null);
            Single.timer(3 * 1000, TimeUnit.MILLISECONDS).subscribe(aLong -> {
                sPool.remove(uri);
            });

            return doLoad();
        } else {
            return Observable.intervalRange(0, 10, 0, 100, TimeUnit.MILLISECONDS).takeUntil(aLong -> sPool.get(uri) != null)
                    .takeLast(1)
                    .flatMap(integer -> {
                        Bitmap bitmap = sPool.get(uri);
                        return bitmap == null ? doLoad() : Observable.just(bitmap);
                    });
        }
    }

    private Observable<Bitmap> doLoad() {
        return Single.just(uri.toString())
                .observeOn(Schedulers.io())
                .flatMap(url -> NetworkUtils.get(url))
                .observeOn(Schedulers.io())
                .map(bytes -> BitmapFactory.decodeByteArray(bytes, 0, bytes.length))
                .observeOn(AndroidSchedulers.mainThread())
                .map(bitmap -> {
                    sPool.put(uri, bitmap);
                    return bitmap;
                })
                .toObservable();
    }
}
