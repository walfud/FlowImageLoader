package com.walfud.flowimageloaderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.walfud.flowimageloader.FlowImageLoader;


public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";

    private ImageView mSunflowerIv;
    private ImageView mSunflowerEffectIv;
    private ImageView mRoseIv;
    private ImageView mRoseEffectIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sunflower
        {
            mSunflowerIv = (ImageView) findViewById(R.id.iv_sunflower);
            mSunflowerEffectIv = (ImageView) findViewById(R.id.iv_sunflower_effect);
            String sunflowerUrl = "https://github.com/walfud/FlowImageLoader/raw/v1.0.0/doc/sunflower.png";

            // Normal loading
            FlowImageLoader.with(this)
                    .load(sunflowerUrl)
                    .into(mSunflowerIv, R.drawable.loading, R.drawable.fail)
                    .cache()
                    .pls();

            // Sweet cake
//            Sunflower.seed(R.drawable.loading, R.drawable.fail);
//            new Sunflower(mSunflowerEffectIv).open(sunflowerUrl);
        }

        // Rose
//        {
            // Normal loading
            mRoseIv = (ImageView) findViewById(R.id.iv_rose);
            mRoseEffectIv = (ImageView) findViewById(R.id.iv_rose_effect);
            String roseUrl = "https://github.com/walfud/FlowImageLoader/raw/v1.0.0/doc/rose.png";
            FlowImageLoader.with(this)
                    .load(roseUrl)
                    .into(mRoseIv, R.drawable.loading, R.drawable.fail)
                    .cache()
                    .pls();
//
//            // Sweet cake
//            Rose.seed(R.drawable.loading, R.drawable.fail);
//            new Rose(mRoseEffectIv).open(roseUrl);
//        }
    }
}
