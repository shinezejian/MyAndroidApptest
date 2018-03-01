package com.zejian.aidl.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zejian on 2018/2/21
 *  * Socket 通信的服务端
 */

public class TCPServerService extends Service {
    private final String TAG = "zejina9";
    private boolean mIsServiceDisconnected;

    String[] msges = {"你是说呢！","你到底是谁呢....","我也不知道呢....","说话，你到底是？"};

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "服务已 create");
        new Thread(new TcpServer()).start();
    }

    private class TcpServer implements Runnable{

        @Override
        public void run() {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDisconnected){
                try {
                    //接收客户端请求
                    final Socket client = serverSocket.accept();
                    new Thread(){
                        @Override
                        public void run() {
                             responseClient(client);
                        }
                    }.start();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    private void responseClient(Socket client)  {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
        //用于接收客户端消息
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //用于给客户端发生消息
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);

        out.println("欢迎来到聊天室。。。");
        while (!mIsServiceDisconnected){
            String string  = null;

                string = in.readLine();

            System.out.println("服务端接收的消息 : "+string);
            if (string == null){
                //客户端断开
                break;
            }

//            int i = new Random().nextInt(msges.length);
//
//            String msg = msges[i];
            out.println("你这句【" + string + "】非常有道理啊！");
            System.out.println("服务端回复了！");

        }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if(out != null){
                    out.close();
                }
                if (in != null){
                    in.close();
                }
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDisconnected =true;
        super.onDestroy();
    }
}
