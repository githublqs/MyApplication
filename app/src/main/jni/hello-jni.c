//
// Created by Administrator on 2016/8/11.
//
#include <android/log.h>
#include "as2_lqs_com_mylibrary_JNITest.h"

#define TAG    "myhello-jni-test" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__) // 定义LOGD类型

JNIEXPORT jstring JNICALL Java_as2_lqs_com_mylibrary_JNITest_getStrFromJni
  (JNIEnv *env, jobject obj){
    char* cstr = "hello from c";
    //LOGD("########## cstr = %s", cstr);
    LOGD("########## ##########");
    return (*env)->NewStringUTF(env, cstr);
  }