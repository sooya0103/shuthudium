package com.example.app.achieve;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class AchieveMonthActivity extends AppCompatActivity {

    private LineChart mChart;

    final long RESET_TIME = -118800000;
    final long DAY = -86400000;

    //共有プリファレンス
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve_month);

        final DateFormat df = new SimpleDateFormat("MM 月");
        final Date date = new Date(System.currentTimeMillis());
        TextView tv2 = findViewById(R.id.TextView2);
        tv2.setText(df.format(date));


        initPref();

        //Dayに行く
        findViewById(R.id.Button1).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        //Weekに行く
        findViewById(R.id.Button2).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AchieveMonthActivity.this, AchieveWeekActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        //Monthに行く
        findViewById(R.id.Button3).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
//                        startActivity(intent);
                    }
                }
        );

        //Yearに行く
        findViewById(R.id.Button4).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AchieveMonthActivity.this, AchieveYearActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        mChart = findViewById(R.id.line_chart);

        // Grid背景色
        mChart.setDrawGridBackground(true);

        // no description text
        mChart.getDescription().setEnabled(true);

        String label[] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

        // Grid縦軸を破線
        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter((label)));


        YAxis leftAxis = mChart.getAxisLeft();
        // Y軸最大最小設定
        leftAxis.setAxisMaximum(15f);
        leftAxis.setAxisMinimum(0f);
        // Grid横軸を破線
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // 右側の目盛り
        mChart.getAxisRight().setEnabled(false);

        // add data
        setData();

        mChart.animateX(2500);
        //mChart.invalidate();

        // dont forget to refresh the drawing
        // mChart.invalidate();

    }

    /************************ここからグラフ******************************************/
    private void setData() {
        // Entry()を使ってLineDataSetに設定できる形に変更してarrayを新しく作成
        long data[] = new long[31];

        //4,6,9,11月の時
        if (getThisMonth() == 4 || getThisMonth() == 6 || getThisMonth() == 9 || getThisMonth() == 11) {
            for (int i = 0; i <= 29; i++) {
                data[i] = getStudyTime(i);
            }
            //1,3,5,7,8,10,12月の時
        } else if (getThisMonth() == 1 || getThisMonth() == 3 || getThisMonth() == 5 || getThisMonth() == 7 || getThisMonth() == 8 || getThisMonth() == 10 || getThisMonth() == 12) {
            for (int i = 0; i <= 30; i++) {
                data[i] = getStudyTime(i);
            }
            //2月の時
        } else {
            for (int i = 0; i <= 27; i++) {
                data[i] = getStudyTime(i);
            }
        }

        for (int i = 0; i < data.length; i++) {
            final TextView tv3 = findViewById(R.id.TextView3);
            long sum = Arrays.stream(data).sum();
            tv3.setText(String.valueOf(sum) + "時間");
        }

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            values.add(new Entry(i, data[i], null, null));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet");

            set1.setDrawIcons(false);
            set1.setColor(Color.BLACK);
            //ラインカラー
            set1.setCircleColor(Color.BLACK);
            //値を表す点の色
            set1.setLineWidth(1f);
            //線の太さ
            set1.setCircleRadius(1f);
            //円の半径
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(0f);
            //各グラフのポイントに値を表示するテキストのサイズ
            set1.setDrawFilled(true);
            //グラフの塗りつぶしをするかしないか
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setFillColor(Color.BLUE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData lineData = new LineData(dataSets);

            // set data
            mChart.setData(lineData);
        }
    }

    /*************************************************************************/
    //共有プリファレンスの初期化
    protected void initPref() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    //yyyy/ mm/ の形で日付を取得
    protected String getDate() {
        DateFormat df = new SimpleDateFormat("yyyy/ MM/ ");
        Date date = new Date(System.currentTimeMillis());

        return df.format(date);
    }

    //今日の日付を入手する
    protected int getThisMonth() {
        DateFormat df = new SimpleDateFormat("MM");
        Date date = new Date(System.currentTimeMillis());

        String tmp = String.valueOf(df.format(date));
        int Month = Integer.parseInt(tmp.substring(0, 2));

        return Month;
    }

    //勉強時間を共有プリファレンスから取ってくる。
    //daysは何日かを指定する
    private int getStudyTime(int days) {
        //整形00:00にする
        SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPANESE);
        long today = pref.getLong(getDate() + (days + 1), RESET_TIME);
        String test = dataFormat.format(today);
        int extractionTime = Integer.parseInt(test.substring(0, 2));


        //分の単位が30を超えていたら時間を1繰り上げる
        return checkExceed30minutes(days) ? extractionTime + 1 : extractionTime;
    }

    //分の値が30超えていたら1繰り上げる
    private boolean checkExceed30minutes(int days) {
        //整形00:00にする
        final SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPANESE);
        //今日の日付の共有プリファレンスを取ってきて、時間を切り取り数字に変換する。
        long tmp = pref.getLong(getDate() + (days + 1), RESET_TIME);
        String test = dataFormat.format(tmp);
        int checkExceed = Integer.parseInt(test.substring(3, 4));

        return checkExceed >= 30 ? true : false;
    }


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