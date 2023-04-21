package com.example.app.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Author: 小池将弘
 * 勉強中画面のフラグメントのモデル
 * ぶっちゃけなくてもいい、
 * 勉強中画面のフラグメントが呼び出されたときの処理を書こうとしたけど特になかった。
 * ButtomNavBarからアクセスできるフラグメントには全部こうしてあるから統一するために残してある
 */

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("この画面をホームにしたい");
    }

    public LiveData<String> getText() {
        return mText;
    }
}