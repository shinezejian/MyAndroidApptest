package com.zejian.aidl.binderpool;

import android.os.RemoteException;

import com.zejian.aidl.ICompute;

public class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

}
