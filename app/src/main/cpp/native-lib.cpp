#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_sl_rakoto_gyroacs_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject, /* this */
        jstring str) {
    std::string hello = "Hello from C++";
    return  str;
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_sl_rakoto_gyroacs_DateService_getAvaregaDate(JNIEnv *env, jobject instance,
                                                       jfloatArray date_) {
    jfloat size = env->GetArrayLength(date_);
    if (size == 0) return 0;

    jfloat result = NULL;
    jfloat *buff = (*env).GetFloatArrayElements(date_, false);


   // env->GetFloatArrayRegion(date_, 0, size, buff);

    for (int i = 0; i < size; i++){
        result += buff[i];
    }

    result /= size;

    buff = NULL;

    return result;
}