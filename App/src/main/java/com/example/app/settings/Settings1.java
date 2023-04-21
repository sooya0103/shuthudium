package com.example.app.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;

/**
 * Author: 小池将弘
 * 設定画面のActivity2
 */
public class Settings1 extends AppCompatActivity {

    LinearLayout goNotification, goVibration;
    TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings1);

        findView();

        goNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings1.this, Settings2.class);
                startActivity(intent);
            }
        });

        goVibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent();
                k.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                k.setData(Uri.parse("package:com.example.app"));
                startActivity(k);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ;
                finish();
            }
        });
    }

    protected void findView() {
        goNotification = findViewById(R.id.goNotification);
        goVibration =  findViewById(R.id.goVibration);
        backButton = findViewById(R.id.backButton);
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