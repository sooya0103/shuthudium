package com.example.app.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.app.R;

/**
 * Author: 小池将弘
 * 通知に関する処理を記述している
 */

public class AlarmNotification extends BroadcastReceiver {

    @Override   // データを受信した
    public void onReceive(Context context, Intent intent) {

        //共有プリファレンス
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int t = pref.getInt("SettingTime", 0);


        /**
         * 送信元でputExtraで渡された値を受け取る
         * 今回は渡された値は名前が"RequestCode"中身の値が1
         * もし、"RequestCode"が存在していなかったら0を受け取る
         **/
        int requestCode = intent.getIntExtra("RequestCode", 0);

        /**
         * PendingIntent:第四引数にはFLAGを設定
         * 既に、同一のPendeingIntentがあった場合の挙動を記述する
         * */
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "default";

        /**
         * System.currentTimeMillis();
         * ミリ秒で表される現在の時間を返す。
         * カウント時間 = 経過時間 – 開始時間でタイマーを作成可能。
         *
         * 参考サイト：https://akira-watson.com/android/system-currenttimemillis.html
         * */
        long currentTime = System.currentTimeMillis();
        Log.d("current", "アラーム時刻はここ" + currentTime);

        /**
         *SimpleDateFormat
         * Dateクラスが保持する日付情報を指定する書式の文字列に変換することができる
         *
         * 参考サイト：https://techacademy.jp/magazine/c
         * */
        //OO:OO:OOの形に変換する
        //SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss", Locale.JAPAN);
        //NotificationManagerのインスタンスを生成
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        /**
         * Uriでファイルパスを取得
         * よくわかんないから、後で調べたい
         * */
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        // Notification　Channel 設定
        NotificationChannel channel = new NotificationChannel(channelId, "お知らせ", NotificationManager.IMPORTANCE_DEFAULT);

        /**
         * .enableVibration:バイブレーションをするかしないか
         * .enableLights:ライトをつけるか
         * .setLightColor:ライトの色を指定
         * .setLockscreenVisibility:ロックされているときに通知を見せるか
         * .setSound:通知をするときの音の指定
         * setShowBadge:バッジを見せる
         * */
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setSound(defaultSoundUri, null);
        channel.setShowBadge(true);
        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);

        /**
         * もし、notificationManagerが生成されていたら（インスタンスが）
         * 通知チャネル（今回は、channel）を渡して通知を登録する
         * */

        //共有プリファレンス書き込み準備
        SharedPreferences.Editor e = pref.edit();
        //共有プリファレンスが1と等しい値だったら書き換える
        if (1 == pref.getInt("AlarmNotificationCount", 0)) {
            e.putInt("AlarmNotificationCount", 0);
            e.commit();

            Log.d("デバッグ", " 共有プリファレンスの値を書き換えて、アラームを実行できるようにしたよ！" + pref.getInt("AlarmNotificationCount", 0));
        }

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context, channelId)
                    /**
                     * .setContentTitle:通知のタイトルを設定
                     * .setContentText:通知のメッセージを設定
                     * .setAutoCancel:ユーザーが通知をタップすると、自動的に通知が消去される
                     * .setContentIntent:タップ時に実行したいPendingIntentを与える
                     * .setWhen:表示する時間
                     * .build:
                     * */
                    .setContentTitle(context.getString(R.string.Encourage_study))
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setContentText(context.getString(R.string.Encourage_text, t))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build();

            //通知をする
            notificationManager.notify(R.string.app_name, notification);


        }
    }
}