package com.zejian.aidl.socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zejian.aidl.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zejian on 2018/2/21
 * Socket 通信的客户端
 */

public class SocketActivity extends AppCompatActivity {

    private Socket mClientSocket;
    private PrintWriter mPrintWriter;
    private SocketHandler mSocketHandler;

    private Button mBtSendSocket;
    private TextView mTvSocketMessage;
    private EditText mEtClientSocket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        mBtSendSocket = findViewById(R.id.bt_send_socket);
        mTvSocketMessage = findViewById(R.id.tv_socket_message);
        mEtClientSocket = findViewById(R.id.et_client_socket);
        mBtSendSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgToSocketServer();
            }
        });
        bindSocketService();
    }

    /**
     * 处理 Socket 线程切换
     */
    @SuppressWarnings("HandlerLeak")
    public class SocketHandler extends Handler {
        public static final int CODE_SOCKET_CONNECT = 1;
        public static final int CODE_SOCKET_MSG = 2;

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case CODE_SOCKET_CONNECT:
                    System.out.println("客户端链接成功！");
                    mBtSendSocket.setEnabled(true);
                    break;
                case CODE_SOCKET_MSG:
                    mTvSocketMessage.setText(mTvSocketMessage.getText() + (String) msg.obj);
                    break;
            }
        }
    }


    private void bindSocketService() {
        //启动服务端
        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);

        mSocketHandler = new SocketHandler();
        new Thread(new Runnable() {    //新开一个线程连接、接收数据
            @Override
            public void run() {
                try {
                    connectSocketServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    /**
     * 通过 Socket 连接服务端
     */
    private void connectSocketServer() throws IOException {
        Socket socket = null;
        while (socket == null) {    //选择在循环中连接是因为有时请求连接时服务端还没创建，需要重试
            try {
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (IOException e) {
                SystemClock.sleep(1_000);
            }
        }

        //连接成功
        mSocketHandler.sendEmptyMessage(SocketHandler.CODE_SOCKET_CONNECT);

        //获取输入流
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (!isFinishing()) {    //死循环监听服务端发送的数据
            final String msg = in.readLine();
            System.out.println("客户端接收到的消息："+msg);
            if (!TextUtils.isEmpty(msg)) {
                //数据传到 Handler 中展示
                mSocketHandler.obtainMessage(SocketHandler.CODE_SOCKET_MSG,
                        "\n" + formatDateTime(System.currentTimeMillis()) + "\nserver : " + msg)
                        .sendToTarget();
            }
        }

        System.out.println("Client quit....");
        mPrintWriter.close();
        in.close();
        socket.close();
    }

    private String formatDateTime(long time){
        return  new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
    }

    public void sendMsgToSocketServer() {
        final String msg = mEtClientSocket.getText().toString();
        if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
            //发送数据，这里注意要在线程中发送，不能在主线程进行网络请求，不然就会报错
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mPrintWriter.println(msg);
                }
            }).start();
            mEtClientSocket.setText("");
            mTvSocketMessage.setText(mTvSocketMessage.getText() + "\n" + formatDateTime(System.currentTimeMillis()) + "\nclient : " + msg);
        }
    }
}
