#include <jni.h>
#include <stdio.h>

#include "Ip_generator.h"
#include "IpgeneratorJNI.h"

//JNIEXPORT jobject JNICALL Java_IpgeneratorJNI_IpgeneratorJNI(JNIEnv *env, jobject thisObj)
//{
//    jclass jcls = env->FindClass("Ip_generatorJNI");
//    jobject newObj = env->AllocObject(jcls);
//    return newObj;
//}

//JNIEXPORT jstring JNICALL Java_IpgeneratorJNI_IPrandom(JNIEnv *env, jobject thisObj)
//{
//   char * Ipgen;
//   Ip_generator *ip_gen = new Ip_generator();
//   Ipgen = ip_gen->IP_random();
//
//   return env->NewStringUTF("Data");
//}

JNIEXPORT jstring JNICALL Java_IpgeneratorJNI_print(JNIEnv *env, jobject obj) {

  return env->NewStringUTF("Data");
}