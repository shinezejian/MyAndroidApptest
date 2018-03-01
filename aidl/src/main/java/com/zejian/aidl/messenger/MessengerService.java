package com.zejian.aidl.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by zejian on 2018/2/20
 * Messenger服务端简单实例,服务端进程
 */

public class MessengerService extends Service {
    private static final String TAG ="wzj" ;
    static final int MSG_SAY_HELLO_FROM_CLIENT = 1;
    static final int MSG_SAY_HELLO_FROM_SERVICER = 2;

    /**
     * 处理客户端发来的请求
     */
    public static class IncomingHandler extends  Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SAY_HELLO_FROM_CLIENT:
                    Bundle data = msg.getData();
                    if (data != null) {
                        Log.i(TAG, data.getString("msg"));
                    }

                    //回复客户端信息,该对象由客户端传递过来
                    Messenger client = msg.replyTo;
                    Message reply = Message.obtain(null,MSG_SAY_HELLO_FROM_SERVICER);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","Service has received the msg!");
                    reply.setData(bundle);
                    try {
                        //发送消息给客户端
                        client.send(reply);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 创建Messenger并传入Handler实例对象
     */
    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * 当绑定Service时,该方法被调用,将通过mMessenger返回一个实现
     * IBinder接口的实例对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service is invoke onBind");
        return mMessenger.getBinder();
    }
}
