package com.zejian.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zejian on 2018/2/20
 * AIDL服务端
 */

public class AIDLService extends Service {
    private static final String TAG="zejian888";

    private  AtomicBoolean mIsServiceDestroy = new AtomicBoolean(false);

    private List<Person> mList;

    private RemoteCallbackList<IOnNewPersonAddListener> mListenerList = new
            RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mList = new CopyOnWriteArrayList<>();
        for (int i = 0; i <3 ; i++) {
            mList.add(new Person("person_"+i,18+i));
        }
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new IManager.Stub() {
        @Override
        public List<Person> getPersonList() throws RemoteException {
            return mList;
        }

        @Override
        public void addPerson(Person person) throws RemoteException {
            if(mList != null && person != null){
                Log.d(TAG,"服务端添加一个会员:"+person.toString());
                mList.add(person);
            }
        }

        @Override
        public void registerListener(IOnNewPersonAddListener listener) throws RemoteException {
            Log.d(TAG,"注册监听");
            mListenerList.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewPersonAddListener listener) throws RemoteException {
            Log.d(TAG,"移除监听");
            mListenerList.unregister(listener);
        }
    };

    @Override
    public void onDestroy() {
        mIsServiceDestroy.set(true);
        super.onDestroy();
    }

    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestroy.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    addNewPerson(new Person());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加新会员通知所有监听器
     * @param person
     * @throws RemoteException
     */
    private void addNewPerson(Person person) throws RemoteException {
        mList.add(person);
        Log.d(TAG,"添加新会员...通知所有监听器");
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N ; i++) {
            IOnNewPersonAddListener l = (IOnNewPersonAddListener) mListenerList.getBroadcastCookie(i);
            if(l != null){
                l.onNewPersonAdd(person);
            }
        }
        mListenerList.finishBroadcast();

    }
}
