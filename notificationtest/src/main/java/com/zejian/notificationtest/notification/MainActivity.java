package com.zejian.notificationtest.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zejian.notificationtest.R;

public class MainActivity extends AppCompatActivity {
    public static final int TYPE_Normal = 1;
    public static final int TYPE_Progress = 2;
    public static final int TYPE_BigText = 3;
    public static final int TYPE_Inbox = 4;
    public static final int TYPE_BigPicture = 5;
    public static final int TYPE_Hangup = 6;
    public static final int TYPE_Media = 7;
    public static final int TYPE_Customer = 8;
    private NotificationManager manger;
    private Intent service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        service = new Intent(this,DownloadService.class);
    }
    public void onClick(View v){
       // Notification.BigPictureStyle, 在细节部分显示一个256dp高度的位图。
        switch (v.getId()){
            case R.id.btn1:
                simpleNotify();
                break;
            case R.id.btn2:
                startService(service);
                break;
            case R.id.btn3:
                bigTextStyle();
                break;
            case R.id.btn4:
                inBoxStyle();
                break;
            case R.id.btn5:
                bigPictureStyle();
                break;
            case R.id.btn6:
                hangup();
                break;
            case R.id.btn7:
                mediaStyle();
                break;
            case R.id.btn8:
                Intent intent = new Intent(this,MediaService.class);
                startService(intent);
                break;
            default:
                break;
        }
    }

    /**
     * Notification.FLAG_SHOW_LIGHTS              //三色灯提醒，在使用三色灯提醒时候必须加该标志符
     Notification.FLAG_ONGOING_EVENT          //发起正在运行事件（活动中）
     Notification.FLAG_INSISTENT   //让声音、振动无限循环，直到用户响应 （取消或者打开）
     Notification.FLAG_ONLY_ALERT_ONCE  //发起Notification后，铃声和震动均只执行一次
     Notification.FLAG_AUTO_CANCEL      //用户单击通知后自动消失
     Notification.FLAG_NO_CLEAR          //只有全部清除时，Notification才会清除 ，不清楚该通知(QQ的通知无法清除，就是用的这个)
     Notification.FLAG_FOREGROUND_SERVICE    //表示正在运行的服务
     */
    private void simpleNotify(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //Ticker是状态栏显示的提示
        builder.setTicker("简单Notification");
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle("标题");
        //第二行内容 通常是通知正文
        builder.setContentText("通知内容");
        //第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
        builder.setSubText("显示摘要内容！");
        builder.setAutoCancel(true);
        //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
        //builder.setContentInfo("2");
        //number设计用来显示同种通知的数量和ContentInfo的位置一样，如果设置了ContentInfo则number会被隐藏
        //设置通知集合的数量，显示在小图标左侧的数字
        builder.setNumber(2);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.push));
        Intent intent = new Intent(this,SettingsActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);
        //点击跳转的intent
        builder.setContentIntent(pIntent);
        //设置震动
        //long[] vibrate = {100,200,100,200};
        //builder.setVibrate(vibrate);
        //通知默认的声音 震动 呼吸灯
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manger.notify(TYPE_Normal,notification);
    }

    /**
     * Notification.BigTextStyle，在细节部分显示一个大的文本块。
     */
    private void bigTextStyle(){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("BigTextStyle");
        builder.setContentText("BigTextStyle演示示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.notification));
        android.support.v4.app.NotificationCompat.BigTextStyle style = new android.support.v4.app.NotificationCompat.BigTextStyle();
        style.bigText("这里是点击通知后要显示的正文，可以换行可以显示很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长");
        style.setBigContentTitle("点击后的标题");
        style.setSummaryText("末尾只一行的文字内容");
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this,SettingsActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manger.notify(TYPE_BigText,notification);
    }

    /**
     * Notification.InboxStyle，在细节部分显示一段行文本
     * 最多显示五行 再多会有截断
     */
    public void inBoxStyle(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("InboxStyle");
        builder.setContentText("InboxStyle演示示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.notification));
        android.support.v4.app.NotificationCompat.InboxStyle style = new android.support.v4.app.NotificationCompat.InboxStyle();
        style.setBigContentTitle("BigContentTitle")
                .addLine("第一行，第一行，第一行，第一行，第一行，第一行，第一行")
                .addLine("第二行")
                .addLine("第三行")
                .addLine("第四行")
                .addLine("第五行")
                .setSummaryText("SummaryText");
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this,SettingsActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manger.notify(TYPE_Inbox,notification);
    }

    public void bigPictureStyle(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("BigPictureStyle");
        builder.setContentText("BigPicture演示示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.notification));
        android.support.v4.app.NotificationCompat.BigPictureStyle style = new android.support.v4.app.NotificationCompat.BigPictureStyle();
        style.setBigContentTitle("BigContentTitle");
        style.setSummaryText("SummaryText");
        style.bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.small));
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this,ImageActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);
        builder.setContentIntent(pIntent);
        Notification notification = builder.build();
        manger.notify(TYPE_BigPicture,notification);
    }

    /***
     * 悬挂式Notification是android5.0新增加的方式，和前两种显示方式不同的是，前两种需要下拉通知栏才能看到通知，
     * 而 悬挂式Notification不需要下拉通知栏就直接显示出来悬挂在屏幕上方并且焦点不变仍在用户操作的界面因此不会打
     * 断用户的操作，过几秒就会自动消失。和前面的Notification不同的是，他需要调用setFullScreenIntent来
     * 将Notification变为悬挂式Notification
     *
     * 1. 此种效果只在5.0以上系统中有效
     2. mainfest中需要添加<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
     3. 可能还需要在设置开启横幅通知权限（在设置通知管理中）
     4. 在部分改版rom上可能会直接弹出应用而不是显示横幅
     */
    private void hangup(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            Toast.makeText(MainActivity.this, "此类通知在Android 5.0以上版本才会有横幅有效！", Toast.LENGTH_SHORT).show();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("横幅通知");
        builder.setContentText("请在设置通知管理中开启消息横幅提醒权限");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.notification));
        Intent intent = new Intent(this,ImageActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);
        builder.setContentIntent(pIntent);
        //需要调用setFullScreenIntent来将Notification变为悬挂式Notification
        builder.setFullScreenIntent(pIntent,true);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        manger.notify(TYPE_Hangup,notification);
    }

    private void mediaStyle(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("MediaStyle");
        builder.setContentText("Song Title");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.notification));
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Intent intent = new Intent(this,ImageActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);
        builder.setContentIntent(pIntent);
        builder.addAction(R.drawable.ic_previous_white,"",null);
        builder.addAction(R.drawable.ic_stop_white,"",null);
        builder.addAction(R.drawable.ic_play_arrow_white_18dp,"",pIntent);
        builder.addAction(R.drawable.ic_next_white,"",null);
//        NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle();
//        style.setMediaSession(new MediaSessionCompat(this,"MediaSession",
//                new ComponentName(MainActivity.this,Intent.ACTION_MEDIA_BUTTON),null).getSessionToken());
//        //CancelButton在5.0以下的机器有效
//        style.setCancelButtonIntent(pIntent);
//        style.setShowCancelButton(true);
//        //设置要现实在通知右方的图标 最多三个
//        style.setShowActionsInCompactView(2,3);
//        builder.setStyle(style);
        builder.setShowWhen(false);
        Notification notification = builder.build();
        manger.notify(TYPE_Media,notification);
    }



    @Override
    protected void onDestroy() {
        if(service!=null){
            stopService(service);
        }
        super.onDestroy();
    }
}
