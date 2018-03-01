package com.zejian.aidl;

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}