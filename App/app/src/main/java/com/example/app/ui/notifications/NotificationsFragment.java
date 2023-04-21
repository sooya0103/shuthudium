package com.example.app.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.settings.HowTo;
import com.example.app.settings.Settings1;

/**
 * Author: 小池将弘
 * 設定画面画面のフラグメント
 */

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    LinearLayout howToUse, goInstagram, goTwitter, goGoogleForm, goSettings1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        // レイアウトをここでViewとして作成
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView();

        howToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "使い方が押されました", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), HowTo.class);
                startActivity(intent);
            }
        });

        goInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Instagramが押されました", Toast.LENGTH_SHORT).show();
                Uri instagram = Uri.parse("https://www.instagram.com/?hl=ja");
                Intent i = new Intent(Intent.ACTION_VIEW, instagram);
                startActivity(i);
            }
        });

        goTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Twitterが押されました", Toast.LENGTH_SHORT).show();
                Uri twitter = Uri.parse("https://twitter.com/?lang=ja");
                Intent j = new Intent(Intent.ACTION_VIEW, twitter);
                startActivity(j);
            }
        });

        goGoogleForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ご意見が押されました", Toast.LENGTH_SHORT).show();
                Uri form = Uri.parse("https://www.google.com/intl/ja_jp/forms/about/");
                Intent k = new Intent(Intent.ACTION_VIEW, form);
                startActivity(k);
            }
        });

        goSettings1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSetting1 = new Intent(getActivity(), Settings1.class);
                startActivity(goSetting1);
            }
        });
    }

    protected void findView() {
        howToUse = getView().findViewById(R.id.HowToUse);
        goInstagram = getView().findViewById(R.id.goInstagram);
        goTwitter = getView().findViewById(R.id.goTwitter);
        goGoogleForm = getView().findViewById(R.id.goGoogleform);
        goSettings1 = getView().findViewById(R.id.goSettings);
    }

}