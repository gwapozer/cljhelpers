#include <stdio.h>
#include <string.h>

#include "TestJNI.h"

JNIEXPORT jstring JNICALL Java_TestJNI_print(JNIEnv *env, jobject obj, jstring name) {
  const char* cname = (*env)->GetStringUTFChars(env, name, 0);
  printf("Hello from C, %s!\n", cname);
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return (*env)->NewStringUTF(env, "Hello from C!");
}

JNIEXPORT jstring JNICALL Java_TestJNI_testprint(JNIEnv *env, jobject obj) {
    char *data = "Some data";
  return (*env)->NewStringUTF(env, data);
}
