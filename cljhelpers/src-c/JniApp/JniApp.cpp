#include "JniApp.h"
#include "Ip_generator.h"

#include <ctype.h>
#include <string.h>

// Mutate array to uppercase
void uppercase(char* str) {
    size_t n = strlen(str);
    for (size_t i = 0; i < n; i++) {
        str[i] = toupper(str[i]);
    }
}

JNIEXPORT jint JNICALL Java_JniApp_intMethod
(JNIEnv* env, jobject obj, jint num) {
    return num * num;
}


JNIEXPORT jboolean JNICALL Java_JniApp_booleanMethod
(JNIEnv* env, jobject obj, jboolean boolean) {
    return !boolean;
}

JNIEXPORT jstring JNICALL Java_JniApp_stringMethod
(JNIEnv* env, jobject obj, jstring string) {
    const char* str = env->GetStringUTFChars(string, 0);
    char cap[128];
    strcpy(cap, str);
    env->ReleaseStringUTFChars(string, str);
    uppercase(cap);
    return env->NewStringUTF(cap);
}

JNIEXPORT jint JNICALL Java_JniApp_intArrayMethod
(JNIEnv* env, jobject obj, jintArray array) {
    int sum = 0;
    jsize len = env->GetArrayLength(array);
    jint* body = env->GetIntArrayElements(array, 0);
    for (int i = 0; i < len; i++) {
        sum += body[i];
    }
    env->ReleaseIntArrayElements(array, body, 0);
    return sum;
}

JNIEXPORT jstring JNICALL Java_JniApp_IpRandom
(JNIEnv* env, jobject obj, jstring string) {

   char * Ipgen;
   Ip_generator *ip_gen = new Ip_generator();
   Ipgen = ip_gen->IP_random();

    const char* str = env->GetStringUTFChars(string, 0);
    char cap[128];
    strcpy(cap, str);
    env->ReleaseStringUTFChars(string, str);
    uppercase(cap);
    return env->NewStringUTF(Ipgen);
}