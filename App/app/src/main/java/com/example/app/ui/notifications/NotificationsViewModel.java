package com.example.app.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Author: 小池将弘
 * 設定画面のフラグメントのモデル
 * ぶっちゃけなくてもいい、
 * 設定画面のフラグメントが呼び出されたときの処理を書こうとしたけど特になかった。
 * ButtomNavBarからアクセスできるフラグメントには全部こうしてあるから統一するために残してある
 * */

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}