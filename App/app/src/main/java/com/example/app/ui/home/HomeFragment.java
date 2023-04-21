package com.example.app.ui.home;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.MyGlobals;
import com.example.app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Author: 小池将弘, 川村健斗
 * 勉強中画面のフラグメント
 */

public class HomeFragment extends Fragment implements SensorEventListener {

    private HomeViewModel homeViewModel;
    //ストップウォッチの開始判定
    int count_sensor = -1;

    //共有プリファレンス
    SharedPreferences pref;
    //グローバル変数
    MyGlobals myGlobals;

    final long RESET_TIME = -118800000;

    //ストップウォッチ関連のフィールド
    private long period = 100, sumStudyTime = RESET_TIME;
    private TextView timerText;
    private final Handler handler = new Handler(Looper.getMainLooper());
    //整形00:00にする
    private final SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPAN);

    //sensorの値を書くフィールド
    protected final static double RAD2DEG = 180 / Math.PI;
    SensorManager sensorManager;

    float[] rotationMatrix = new float[9];
    float[] gravity = new float[3];
    float[] geomagnetic = new float[3];
    float[] attitude = new float[3];
    private int phoneTiltHome;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // レイアウトをここでViewとして作成
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initMyGlobals();
        initPref();
        initSensor();
        findView();

        //STARTボタン
        view.findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_sensor = 0;
            }
        });

        //STOPボタン
        view.findViewById(R.id.stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                count_sensor = 1;
            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
        }

        if (geomagnetic != null && gravity != null) {
            SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);
            SensorManager.getOrientation(rotationMatrix, attitude);

            //端末の傾き
            phoneTiltHome = (int) (attitude[1] * RAD2DEG);

            if (count_sensor == 0 && phoneTiltHome == 0) {
                handler.post(runnable);
                count_sensor = 1;
                Log.d("センサー✅", "センサーでストップウォッチが動いてます！！" + count_sensor);
                return;
            }

            if (count_sensor == 1 && phoneTiltHome != 0) {
                handler.removeCallbacks(runnable);
                count_sensor = 0;
                Log.d("センサー✅", "センサーでストップウォッチが止まってます！！" + count_sensor);
                return;
            }
        }
    }


    /*******************************************************************/
    //ストップウォッチ
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            myGlobals.Count++;
            sumStudyTime = myGlobals.Count * period + RESET_TIME;
            handler.postDelayed(this, period);
            timerText.setText(dataFormat.format(sumStudyTime));
            Log.d("runnable", "run: " + myGlobals.Count);
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //実装するだけ
    }


    //画面が表示されるときに行われる処理
    @Override
    public void onStart() {
        super.onStart();

        //累計時間の初期化
        if (pref.getLong(getToday(), RESET_TIME) == RESET_TIME) {
            commitPrefLong(getToday(),RESET_TIME);
        }

        //今日の日付のtimeを取り出し、画面に入れる
        timerText.setText(dataFormat.format(pref.getLong(getToday(),RESET_TIME)));

        if (pref.getLong(getToday(), RESET_TIME) == RESET_TIME) {
            commitPrefInt("Count of the day", 0);
            myGlobals.Count = pref.getInt("Count of the day", 0);
        }
    }

    //ユーザー操作が行えなくなった際に呼び出される。
    @Override
    public void onPause() {
        super.onPause();

        handler.removeCallbacks(runnable);

        commitPrefLong(getToday(), sumStudyTime);
        commitPrefInt("Count of the day", myGlobals.Count);
    }

    //共有プリファレンスの初期化
    protected void initPref() {
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    //センサーの初期化
    protected void initSensor() {
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);

    }

    //グローバル変数の初期化
    protected void initMyGlobals() {
        //グローバル変数
        myGlobals = (MyGlobals) getActivity().getApplicationContext();
    }

    //xml紐づけ
    protected void findView() {
        timerText = getView().findViewById(R.id.timerView);
    }

    //今日の年月日を取得する
    protected String getToday() {
        DateFormat df = new SimpleDateFormat("yyyy/ MM/ dd");
        Date date = new Date(System.currentTimeMillis());

        return df.format(date);
    }

    //prefをcommitする(Long型)
    public void commitPrefLong(String key, long value) {
        SharedPreferences.Editor e = pref.edit();
        e.putLong(key, value);
        e.commit();
    }

    //prefをcommitする(int型)
    public void commitPrefInt(String key, int value) {
        SharedPreferences.Editor e = pref.edit();
        e.putInt(key, value);
        e.commit();
    }
}