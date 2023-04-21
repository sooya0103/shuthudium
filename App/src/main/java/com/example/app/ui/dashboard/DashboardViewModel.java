package com.example.app.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Author: 小池将弘
 * 実績画面のフラグメントのモデル
 * ぶっちゃけなくてもいい、
 * 実装画面のフラグメントが呼び出されたときの処理を書こうとしたけど特になかった。
 * ButtomNavBarからアクセスできるフラグメントには全部こうしてあるから統一するために残してある
 * */

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("ここを実績のところにしたい");
    }

    public LiveData<String> getText() {
        return mText;
    }
}