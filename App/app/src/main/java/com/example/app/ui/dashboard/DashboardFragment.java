package com.example.app.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.achieve.AchieveMonthActivity;
import com.example.app.achieve.AchieveWeekActivity;
import com.example.app.achieve.AchieveYearActivity;
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

/**
 * Author: 小池将弘、柳沢孝弘
 * 実績画面のフラグメント
 */

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private LineChart mChart;

    final long RESET_TIME = -118800000;


    public TextView tv3;

    //共有プリファレンス
    SharedPreferences pref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initPref();

        final DateFormat df = new SimpleDateFormat("MM / dd");
        final Date date = new Date(System.currentTimeMillis());

        TextView tv2 = view.findViewById(R.id.TextView2);
        tv2.setText(df.format(date));
        tv3 = view.findViewById(R.id.TextView3);

        Button toDay = view.findViewById(R.id.Button1);
        Button toWeek = view.findViewById(R.id.Button2);
        Button toMonth = view.findViewById(R.id.Button3);
        Button toYear = view.findViewById(R.id.Button4);

        toDay.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), getActivity());
//                        startActivity(intent);
                    }
                }
        );
        toWeek.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AchieveWeekActivity.class);
                        startActivity(intent);
                    }
                }
        );
        toMonth.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AchieveMonthActivity.class);
                        startActivity(intent);
                    }
                }
        );
        toYear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AchieveYearActivity.class);
                        startActivity(intent);
                    }
                }
        );

        mChart = view.findViewById(R.id.line_chart);

        // Grid背景色
        mChart.setDrawGridBackground(true);


        // no description text
        mChart.getDescription().setEnabled(true);

        //String label[] = new String[]{df.format(date)};

        // Grid縦軸を破線
        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setValueFormatter(new IndexAxisValueFormatter((String[]) null));

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


    @Override
    public void onStart() {
        super.onStart();
    }

    private void setData() {


        // Entry()を使ってLineDataSetに設定できる形に変更してarrayを新しく作成
//        int data[] = {8, 2, 1, 12, 10, 8,
//                9, 10, 7, 10, 12, 11,
//                10, 10, 10, 9, 8, 9,
//        };

        int data[] = new int[1];
        data[0] = getStudyTimeToday();
//        int s = 0;
//        for(int i = 0; i < data.length; i++){
//             s = s + data[i];
//        }
//        TextView tv3 = findViewById(R.id.TextView3);
//        tv3.setText(s);

//        for(int i = 0; i < data.length; i++){
//            int sum = Arrays.stream(data).reduce(0,(a,b)-> a + b);
//            TextView tv3 = findViewById(R.id.TextView3);
//            tv3.setText(sum);
//
//        }
//
        for (int i = 0; i < data.length; i++) {
            int sum = Arrays.stream(data).sum();
            tv3.setText(String.valueOf(sum) + "時間");
        }

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {

            values.add(new Entry(i, data[i], null, null));
        }

        LineDataSet set1;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            //値の取得　変更
            mChart.notifyDataSetChanged();
            //値の反映

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
            set1.setCircleRadius(3f);
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

    //共有プリファレンスの初期化
    protected void initPref() {
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    //今日の年月日を取得する
    protected String getToday() {
        DateFormat df = new SimpleDateFormat("yyyy/ MM/ dd");
        Date date = new Date(System.currentTimeMillis());

        return df.format(date);
    }

    //勉強時間を共有プリファレンスから取ってくる。
    private int getStudyTimeToday() {
        //整形00:00にする
        final SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPANESE);
        //今日の日付の共有プリファレンスを取ってきて、時間を切り取り数字に変換する。
        long tmp = pref.getLong(getToday(), RESET_TIME);
        String test = dataFormat.format(tmp);
        int extractionTime = Integer.parseInt(test.substring(0, 2));


        return checkExceed30minutes() ? extractionTime + 1 : extractionTime;
    }

    private boolean checkExceed30minutes(){
        //整形00:00にする
        final SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPANESE);
        //今日の日付の共有プリファレンスを取ってきて、時間を切り取り数字に変換する。
        long tmp = pref.getLong(getToday(), RESET_TIME);
        String test = dataFormat.format(tmp);
        int checkExceed = Integer.parseInt(test.substring(3, 4));

        return checkExceed >= 30 ? true : false;
    }


}