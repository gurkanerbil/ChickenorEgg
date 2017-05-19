package com.erbil.chickenoregg;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private LinearLayout mLayoutBucket;
    private int mWidthScreen;
    private ImageView mImageChicken;
    private ImageView mImageEgg;
    private int tmp;
    private int rndTmp = -1;
    private int score = 0;
    private int timer = 2000;
    private TextView textScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textScore = (TextView) findViewById(R.id.text_score);
        mImageEgg = (ImageView) findViewById(R.id.image_egg);
        mImageChicken = (ImageView) findViewById(R.id.image_chicken);
        mLayoutBucket = (LinearLayout) findViewById(R.id.layout_bucket);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mWidthScreen = display.getWidth();

        tmp = mWidthScreen / 2 - 40;
        startTime();
        textScore.setText("" + score);
    }

    private Handler mHandler = new Handler();

    private void startTime() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 0);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (score % 3 == 0) {
                final int randomSpeed = (new Random()).nextInt(6);

                if (randomSpeed == 0)
                    timer = 3000;
                else if (randomSpeed == 1)
                    timer = 2500;
                else if (randomSpeed == 2)
                    timer = 2000;
                else if (randomSpeed == 3)
                    timer = 1500;
                else if (randomSpeed == 4)
                    timer = 1000;
                else if (randomSpeed == 5)
                    timer = 500;
            }

            if (rndTmp != -1) {
                if (mLayoutBucket.getX() < mImageEgg.getX() && mLayoutBucket.getX() >= mImageEgg.getX() - 128) {
                    score++;
                    textScore.setText("" + score);
                }
                mImageEgg.setX(rndTmp + 32);
            }
            final int rand = (new Random()).nextInt(mWidthScreen - 128);
            mImageChicken.animate().setDuration(timer).translationX(rand);
            mImageEgg.setY(128);
            mImageEgg.animate().setDuration(timer).translationY(900);
            rndTmp = rand;
            mHandler.postDelayed(this, timer);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float mAx = (-1) * (event.values[0]) / 10;

        if (mAx > 0) {
            tmp += (int) (((mWidthScreen - 64 - tmp) * mAx) / 4);
            mLayoutBucket.setX(tmp);
        } else if (mAx < 0) {
            tmp += (int) ((tmp * mAx) / 4);
            mLayoutBucket.setX(tmp);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}