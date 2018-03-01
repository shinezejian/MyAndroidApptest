package com.zejian.notificationtest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button smallNotic;
    private Button bigNotic;
    NotificationManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smallNotic = findViewById(R.id.smallNotific);
        bigNotic = findViewById(R.id.bigNotific);
        smallNotic.setOnClickListener(this);
        bigNotic.setOnClickListener(this);
        mManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

//       3.0之前用法
//        NotificationManager mNotifyMgr =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        PendingIntent contentIntent = PendingIntent.getActivity(
//                this, 0, new Intent(this, ResultActivity.class), 0);
//
//        Notification notification = new Notification(icon, tickerText, when);
//        notification.setLatestEventInfo(this, title, content, contentIntent);
//
//        mNotifyMgr.notify(NOTIFICATIONS_ID, notification);


        //Android 3.0开始弃用new Notification()方式，改用Notification.Builder()来创建通知:
        //这里需要注意: "build()" 是Androdi 4.1(API level 16)加入的，用以替代
        //"getNotification()"。API level 16开始弃用"getNotification()"
//        NotificationManager mNotifyMgr =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        PendingIntent contentIntent = PendingIntent.getActivity(
//                this, 0, new Intent(this, ResultActivity.class), 0);
//
//        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle("My notification")
//                .setContentText("Hello World!")
//                .setContentIntent(contentIntent)
//                .build();// getNotification()
//
//        mNotifyMgr.notify(NOTIFICATIONS_ID, notification);


      //为了兼容API level 11之前的版本，v4 Support Library中提供了
      // NotificationCompat.Builder()这个替代方法。它与Notification.Builder()类似，二者没有太大区别。
//
//        Intent resultIntent = new Intent(this, DemoActivity.class);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //NotificationCompat.Builder自动设置的默认值:
        //priority: PRIORITY_DEFAULT
        //when: System.currentTimeMillis()
        //audio stream: STREAM_DEFAULT
//        NotificationCompat.Builder mBuilder =new  NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("Notification title")
//                .setContentText("text text")
//                .setContentIntent(resultPendingIntent);
//
//        NotificationManager manager = (NotificationManager)
//                getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(1,mBuilder.build());
    }

    public void smallNotic(){

        //为了兼容API level 11之前的版本，v4 Support Library中提供了
        // NotificationCompat.Builder()这个替代方法。它与Notification.Builder()类似，二者没有太大区别。
        Intent resultIntent = new Intent(this, DemoActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //NotificationCompat.Builder自动设置的默认值:
        //priority: PRIORITY_DEFAULT
        //when: System.currentTimeMillis()
        //audio stream: STREAM_DEFAULT
        NotificationCompat.Builder mBuilder =new  NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("small icon Notification title")
                .setContentText("samall icon text text ")
                .setContentIntent(resultPendingIntent);
        mManager.notify(1,mBuilder.build());
    }


    public void bigNotic(){
        Intent resultIntent = new Intent(this, DemoActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("big big notice")
                .setContentText("Notification可以让我们在获得消息的时候，在状态栏，锁屏界面来显示相应的信息，很难想象如果没有Notification，那我们的qq和微信以及其他应用没法主动通知我们，我们就需要时时的看手机来检查是否有新的信息和提醒着实让人烦心，也体现出Notification重要性。这里会介绍三种Notification，分别是普通的Notification，折叠式Notification和悬挂式Notification。")
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
//                .setStyle()
                ;
        mManager.notify(2,mBuilder.build());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.smallNotific:
                smallNotic();
                break;
            case R.id.bigNotific:
                bigNotic();
                break;

        }
    }
}
