package com.example.app.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.MyGlobals;
import com.example.app.R;

/**
 * Author: 小池将弘
 * 設定画面のActivity2
 */
public class Settings2 extends AppCompatActivity implements View.OnClickListener {

    MyGlobals myGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        //共有プリファレンス
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        //共有プリファレンス書き込み準備
        SharedPreferences.Editor e = pref.edit();

        //グローバル変数
        myGlobals = (MyGlobals) this.getApplication();

        //以下から各要素の結びつけ
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        EditText settingsTime = (EditText) findViewById(R.id.settingsTime);
        TextView backButton = (TextView) findViewById(R.id.backButton);
        Button commitTimeButton = (Button) findViewById((R.id.commitTime));

        tv1.setOnClickListener(this);
        backButton.setOnClickListener(this);
        settingsTime.setOnClickListener(this);
        commitTimeButton.setOnClickListener(this);

        //起動時、前回までに設定した時間を設定する。
        settingsTime.setText(String.valueOf(pref.getInt("SettingTime", 0)));


        commitTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //設定された値を読み込む
                String time = settingsTime.getText().toString();

                //入力欄が空白だったら、記入するように促す
                if (time.equals("")) {
                    Toast.makeText(Settings2.this, time + "時刻を設定してください。", Toast.LENGTH_SHORT).show();
                    Log.d("w", "onClick: ココが入力がなかったときに来る処理");
                    //入力欄に何か記入されていたら実行
                } else {
                    //入力された文字列長を取得
                    for (int i = 0; i < time.length(); i++) {
                        //入力された文字列の中に数字以外が合ったら数字を入力させるように促す
                        if (!Character.isDigit(time.charAt(i))) {
                            settingsTime.setText("");
                            Toast.makeText(Settings2.this, "数字を入力してください", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Integer.parseInt(time) > 180) {
                                settingsTime.setText("");
                                Toast.makeText(Settings2.this, "設定上限は180分までです。", Toast.LENGTH_SHORT).show();
                            } else {
                                settingsTime.setText(time);
                                Toast.makeText(Settings2.this, time + "分 に設定されました。", Toast.LENGTH_SHORT).show();
                            }

                            //入力されている値が共有プリファレンスと異なる値だったら書き換える
                            if (Integer.parseInt(time) != pref.getInt("SettingTime", 0)) {
                                //グローバル変数に設定した値を入れてその値を用いて、共有プリファレンスに入れる
                                //わざわざ、グローバル変数の値をputするのは、prefとグローバル変数で整合性を保ちたかったから
                                myGlobals.SettingTime = Integer.parseInt(time);
                                e.putInt("SettingTime", myGlobals.SettingTime);
                                e.commit();
                            }

                        }
                    }
                }


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;


            case R.id.settingsTime:
                EditText edit = findViewById(R.id.settingsTime);
                edit.setText("");

        }
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