package com.walfud.flowimageloader.dna.gene;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.walfud.flowimageloader.dna.Dna;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by walfud on 2016/3/19.
 */
public class ResizeGene extends Gene {
    public int dstWidth;
    public int dstHeight;

    public ResizeGene(int dstWidth, int dstHeight) {
        this.dstWidth = dstWidth;
        this.dstHeight = dstHeight;
    }

    @Override
    public Observable<Bitmap> onInject(Dna dna) {
        return Observable.just(0)
                .observeOn(Schedulers.computation())
                .map(object -> {
                    Bitmap src = dna.getBitmap();
                    int srcWidth = src.getWidth();
                    int srcHeight = src.getHeight();
                    Bitmap.Config originConfig = src.getConfig();

                    Bitmap dst = Bitmap.createBitmap(dstWidth, dstHeight, originConfig);
                    Canvas canvas = new Canvas(dst);

                    // Calculate scale value
                    double scaleX = (double) dstWidth / srcWidth;
                    double scaleY = (double) dstHeight / srcHeight;

                    Matrix matrix = new Matrix();
                    matrix.setScale((float) scaleX, (float) scaleY);
                    canvas.setMatrix(matrix);
                    canvas.drawBitmap(src, 0, 0, null);

                    return dst;
                });
    }
}
