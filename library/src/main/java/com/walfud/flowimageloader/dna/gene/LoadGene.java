package com.walfud.flowimageloader.dna.gene;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.walfud.flowimageloader.FlowImageLoader;
import com.walfud.flowimageloader.dna.Dna;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.schedulers.Schedulers;

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
        return Observable.just(null)
                .observeOn(Schedulers.io())
                .map(aVoid -> {
                    Bitmap bitmap = null;

                    Request request = new Request.Builder()
                            .url(uri.toString())
                            .get()
                            .build();
                    try {
                        Response response = FlowImageLoader.getOkHttpClientInstance().newCall(request).execute();
                        if (response.isSuccessful()) {
                            // Success
                            ResponseBody responseBody = response.body();
                            byte[] imageFile = responseBody.bytes();

                            // Decode
                            bitmap = BitmapFactory.decodeByteArray(imageFile, 0, imageFile.length);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return bitmap;
                });
    }
}
