package com.zejian.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by zejian on 2018/2/20
 * AIDL客户端
 *
 */

public class MainActivity extends AppCompatActivity {

    IManager iManager;
    static final int MESSAGE_NEW_PERSON_ADD = 1;
//    private ServiceConnection mConnection = null;

    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_PERSON_ADD:
                    Log.d("zejian888","客户端接收到新添加会员的消息");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,AIDLService.class);
        //绑定服务端
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iManager != null){
                    try {
                        iManager.addPerson(new Person("super man",28));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iManager != null){
                    try {
                       Log.d("zejian88","获取服务端的会员："+iManager.getPersonList().toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     * 客户端链接服务端的类
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        /**
         * 系统会调用该方法以传递服务的onBind() 方法返回的 IBinder。其中service便是服务端返回的IBinder实现类对象，
         * 通过该对象我们便可以调用获取LocalService实例对象，进而调用服务端的公共方法。而ComponentName是一个封装了
         * 组件(Activity, Service, BroadcastReceiver, or ContentProvider)信息的类，如包名，组件描述等信息，
         * 较少使用该参数。
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service == null) return;

            iManager = IManager.Stub.asInterface(service);
            try {
                //设置死亡监听
                service.linkToDeath(mDeathRecipient,0);
                //注册监听
                iManager.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * Android 系统会在与服务的连接意外中断时（例如当服务崩溃或被终止时）调用该方法。注意:当客户端取消绑定时，系统“绝对不会”调用该方法。
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mConnection != null){
                unbindService(mConnection);
            }
            //重新绑定
            Intent intent = new Intent(MainActivity.this,AIDLService.class);
            //绑定服务端
            bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        }
    };

    /**
     * 跨进程监听器,需要通过handler切换到UI线程处理相关业务
     */
    private IOnNewPersonAddListener listener = new IOnNewPersonAddListener.Stub() {

        @Override
        public void onNewPersonAdd(Person person) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_PERSON_ADD,person).sendToTarget();
        }
    };

    /**
     * 用于处理服务端Binder意外死亡后重新绑定服务
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if(iManager !=null){
                //先解出意味挂掉监听
                iManager.asBinder().unlinkToDeath(mDeathRecipient,0);
                iManager =null;

                //重新绑定
                Intent intent = new Intent(MainActivity.this,AIDLService.class);
                //绑定服务端
                bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
            }
        }
    };

    @Override
    protected void onDestroy() {

        if (iManager != null && iManager.asBinder().isBinderAlive()){
            try {
                //移除监听
                iManager.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //解除绑定
        unbindService(mConnection);
        super.onDestroy();

    }


}
