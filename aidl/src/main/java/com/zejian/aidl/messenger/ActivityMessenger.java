package com.zejian.aidl.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zejian.aidl.R;

/**
 * Created by zejian on 2018/2/20
 * Messenger 客户端
 *
 *
 * 1.服务实现一个 Handler，由其接收来自客户端的信息

 2.Handler 用于创建 Messenger 对象（对 Handler 的引用）

 3.Messenger 创建一个 IBinder，服务通过 onBind() 使其返回客户端

 4.客户端使用 IBinder 将 Messenger（引用服务的 Handler）实例化，然后使用Messenger将 Message 对象发送给服务

 5.服务在其 Handler 中（在 handleMessage() 方法中）接收每个 Message
 */

public class ActivityMessenger extends AppCompatActivity {
    private static final String TAG ="wzj" ;
    /**
     * 服务端交互的Messenger
     */
    Messenger mService = null;

    /**
     * 客户端的Messenger
     */
    private Messenger mRecevierReplyMsg= new Messenger(new ReceiverReplyMsgHandler());


    private static class ReceiverReplyMsgHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessengerService.MSG_SAY_HELLO_FROM_SERVICER:
                    Log.i(TAG, msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }

            super.handleMessage(msg);
        }
    }


    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        Intent intent = new Intent(this,MessengerService.class);
        bindService(intent,mConn, Context.BIND_AUTO_CREATE);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建通信实体
                Message msg = Message.obtain(null,MessengerService.MSG_SAY_HELLO_FROM_CLIENT);
                Bundle bundle = new Bundle();
                bundle.putString("msg","send msg from client");
                msg.setData(bundle);
                //把接收服务器端的回复的Messenger通过Message的replyTo参数传递给服务端
                msg.replyTo=mRecevierReplyMsg;
                try {
                    //发送消息给服务端
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConn);
    }
}
