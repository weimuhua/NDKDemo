#include <baidu_com_ndkdemo_NativeMethod.h>
#include <android/log.h>
#define LOG_TAG "LogFromC"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

    JNIEXPORT jstring JNICALL Java_baidu_com_ndkdemo_NativeMethod_getNativeStr(JNIEnv * env, jobject obj) {
        return (*env)->NewStringUTF(env,"Hello From Native!");
    }

    JNIEXPORT void JNICALL Java_baidu_com_ndkdemo_NativeMethod_callJavaCode(JNIEnv * env, jobject obj) {
        char* classname = "baidu/com/ndkdemo/NativeMethod";
        jclass clazz = (*env)->FindClass(env,classname);
        if (clazz == 0) {
            LOGI("not found class!");
        } else {
            LOGI("found class");
        }
        jmethodID methodID = (*env)->GetMethodID(env,clazz,"printStrFromNative","(Ljava/lang/String;)V");
        if (methodID == 0) {
               LOGI("not found method!");
        } else {
               LOGI("found method");
        }
        (*env)->CallVoidMethod(env,obj,methodID,(*env)->NewStringUTF(env,"Native String From JNI, hahaha! You Got it!"));
    }