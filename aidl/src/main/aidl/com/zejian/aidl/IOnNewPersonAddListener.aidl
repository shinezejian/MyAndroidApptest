// IOnNewPersonAddListener.aidl
package com.zejian.aidl;

// Declare any non-default types here with import statements

import com.zejian.aidl.Person;
//监听回调
interface IOnNewPersonAddListener {
   void onNewPersonAdd(in Person person);
}
