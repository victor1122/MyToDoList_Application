#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fpt_anhnht_assignment2_1se61750_1todolist_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
