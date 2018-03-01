package com.zejian.notificationtest.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.zejian.notificationtest.R;

/**
 * Created by zejian on 2018/2/27
 * 桌面小部件，AppWidgetProvider本质是广播
 * 先定义部件文件widget_layout.xml，再定义/xml/appwidget_provider_info.xml 部件文件
 */

public class MyAppWidgetProvider extends AppWidgetProvider {

    public static final String TAG = "MyAppWidgetProvider";
    public static final String CLICK_ACTION = "com.zejian.action.CLICK";

    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent == null) return;;
        Log.i(TAG, "onReceive : action = " + intent.getAction());

        //这里判断是自己的action，做自己的事情，比如小工具被点击了要干啥，这里是做一个动画效果
        if(intent.getAction().equals(CLICK_ACTION)){
            Toast.makeText(context, "clicked it", Toast.LENGTH_SHORT).show();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon1);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                    //360度旋转动画
                    for (int i = 0; i < 37; i++) {
                        float degree = (i * 10) % 360;
                        //控件布局
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
                        remoteViews.setImageViewBitmap(R.id.imageView1,rotateBitmap(context,srcBitmap,degree));
                        Intent intentClick = new Intent();
                        intentClick.setAction(CLICK_ACTION);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intentClick,0);
                        //设置图片点击事件
                        remoteViews.setOnClickPendingIntent(R.id.imageView1,pendingIntent);

                        appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidgetProvider.class),remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }).start();
        }


    }
    /**
     * 每次窗口小部件被点击更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate");

        final int counter = appWidgetIds.length;
        Log.i(TAG, "counter = " + counter);
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }

    }

    /**
     * 窗口小部件更新
     *
     * @param context
     * @param appWidgeManger
     * @param appWidgetId
     */
    private void onWidgetUpdate(Context context,
                                AppWidgetManager appWidgeManger, int appWidgetId) {

        Log.i(TAG, "appWidgetId = " + appWidgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        // "窗口小部件"点击事件发送的Intent广播
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intentClick, 0);
        remoteViews.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
        appWidgeManger.updateAppWidget(appWidgetId, remoteViews);
    }


    private Bitmap rotateBitmap(Context context, Bitmap srcbBitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap tmpBitmap = Bitmap.createBitmap(srcbBitmap, 0, 0,
                srcbBitmap.getWidth(), srcbBitmap.getHeight(), matrix, true);
        return tmpBitmap;
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     * 可添加多次，但只在第一次调用
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 每删除一次桌面小部件就调用一次
     * @param context
     * @param appWidgetIds
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个该类型的桌面小部件被删除时调用该方法，注意是最后一个。
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
