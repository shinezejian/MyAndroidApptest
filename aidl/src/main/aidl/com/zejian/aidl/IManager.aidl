// ILogManager.aidl
package com.zejian.aidl;

// Declare any non-default types here with import statements

//导包
import com.zejian.aidl.Person;
import com.zejian.aidl.IOnNewPersonAddListener;


interface IManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

   List<Person> getPersonList();

   void addPerson(in Person person);

   //注册监听器
   void registerListener(IOnNewPersonAddListener listener);

   //移除监听器
   void unregisterListener(IOnNewPersonAddListener listener);



}
