#include <jni.h>
#include <sstream>
#include <vector>

std::string jstringToString(JNIEnv* env, jstring jStr) {
    const char* str = env->GetStringUTFChars(jStr, nullptr);
    std::string result(str);
    env->ReleaseStringUTFChars(jStr, str);
    return result;
}

std::vector<std::string> jobjectArrayToVector(JNIEnv* env, jobjectArray jArray) {
    jsize arrayLength = env->GetArrayLength(jArray);
    std::vector<std::string> result;
    for (jsize i = 0; i < arrayLength; i++) {
        jstring jStr = (jstring) env->GetObjectArrayElement(jArray, i);
        std::string str = jstringToString(env, jStr);
        result.push_back(str);
        env->DeleteLocalRef(jStr);
    }
    return result;
}
