package com.example.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.app.alarm.AlarmNotification;
import com.example.app.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Author: 小池将弘,川村健斗
 * MainActivity
 * 画面のタイトルバーとアクションバーを消す処理と、
 * 3つのフラグメント（勉強中、実績、設定）について記述している。
 * <p>
 * ストップウォッチ機能
 * 開始ボタンを押したらストップウォッチを開始し、
 * 停止ボタンを押したらストップウォッチが停止する
 * また、画面の傾きによってストップウォッチを開始、停止する
 */


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //共有プリファレンス
    SharedPreferences pref;

    //alarm関連のフィールド
    private AlarmManager am;
    private PendingIntent pending;
    private int requestCode = 1;
    private int alertTime;

    //sensorの値を書くフィールド
    protected final static double RAD2DEG = 180 / Math.PI;
    SensorManager sensorManager;

    float[] rotationMatrix = new float[9];
    float[] gravity = new float[3];
    float[] geomagnetic = new float[3];
    float[] attitude = new float[3];
    private int phoneTilt;

    /*****************************************************************/
    @Override
    //起動時に自動でやるところ
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //フラグメントの初期化
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        initSensor();

        //共有プリファレンス
        //設定済みの時間を起動時に代入する
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        alertTime = pref.getInt("SettingTime", 0);

        //アラームカウントの初期化
        pref.edit().remove("AlarmNotificationCount").commit();
    }

    /***************************************************************/
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //実装するだけ今回は何も処理を書くことはない
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

            phoneTilt = (int) (attitude[1] * RAD2DEG);

            //端末が傾いたら && 共有プリファレンスの値が0だったら
            if (pref.getInt("AlarmNotificationCount", 0) == 0 && phoneTilt != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                // 何秒後に通知を送るかの設定
                calendar.add(Calendar.SECOND, alertTime);

                Intent intent = new Intent(this, AlarmNotification.class);

                /**
                 * 情報を受け取るActivityはonCreateメソッド等でgetIntentメソッドを使ってIntentを取得し、
                 * 受け取る情報のデータ型に合ったメソッドで情報を取得する。
                 * 例えば、String型ならばgetStringExtraメソッドに情報をセットしたときの名前を使って情報を受け取る。
                 * ex)Intent intent = getIntent();
                 *
                 *   参考サイト
                 *   https://www.office-matsunaga.biz/android/description.php?id=23
                 * */

                //putExtra:RequestCodeを名前として第二引数の値をインテントに渡す
                intent.putExtra("RequestCode", requestCode);

                pending = PendingIntent.getBroadcast(this, requestCode, intent, 0);
                // アラームをセットする
                am = (AlarmManager) this.getSystemService(ALARM_SERVICE);

                //アラームがセットされていたら実行
                if (am != null) {
                    am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
                    commitPref("AlarmNotificationCount", 1);
                }
            }

            //端末の傾きが0、カウントが1だったら発行されたアラームをキャンセルする
            if (pref.getInt("AlarmNotificationCount", 0) == 1 && phoneTilt == 0) {
                Intent indent = new Intent(this, AlarmNotification.class);
                PendingIntent pending = PendingIntent.getBroadcast(this, requestCode, indent, 0);

                // アラームを解除する
                AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
                if (am != null) {
                    am.cancel(pending);
                    commitPref("AlarmNotificationCount", 0);
                }
            }
        }
    }

    //onPauseでイベントリスナーの解除をする（これを忘れるとバッテリーの無駄な消費になる）
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    //センサーの初期化
    protected void initSensor() {
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);

    }

    public void commitPref(String key, int value) {
        SharedPreferences.Editor e = pref.edit();
        e.putInt(key, value);
        e.commit();
    }

/*********************************************************************/
    /**
     * 以下、タイトルバー及び、アクションバーの削除
     * 参考サイト
     * https://akira-watson.com/android/theme-notitlebar.html#4
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }
}
