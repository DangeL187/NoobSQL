#include <jni.h>
#include <string>
#include <filesystem>
#include <functional>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/log.h>

#define jni_int extern "C" JNIEXPORT jint JNICALL
#define jni_object_array extern "C" JNIEXPORT jobjectArray JNICALL
#define jni_object extern "C" JNIEXPORT jobject JNICALL
#define LOGV(TAG, ...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)

#include "Database/Database.hpp"
#include "maxLength.hpp"
#include "jToCpp.hpp"

std::shared_ptr<Database> db;

jni_int Java_com_example_noobsql_MainActivity_initDatabase(JNIEnv* env, jobject /* this */, jstring filePath) {
    try {
        const char* file_path = env->GetStringUTFChars(filePath, nullptr);
        db = std::make_shared<Database>(file_path);
        LOGV("DATABASE::INITIALIZED", "%s", file_path);
    } catch (std::exception& e) {
        LOGV("DATABASE::ERROR", "%s", e.what());
        return -1;
    }
    return 0;
}

jni_object_array Java_com_example_noobsql_MainActivity_getTableNames(JNIEnv* env, jobject /* this */) {
    std::vector<std::string> tables = db->getTableNames();

    jclass stringClass = env->FindClass("java/lang/String");
    jobjectArray stringArray = env->NewObjectArray(tables.size(), stringClass, nullptr);
    for (int i = 0; i < tables.size(); i++) {
        jstring string = env->NewStringUTF(tables[i].c_str());
        env->SetObjectArrayElement(stringArray, i, string);
        env->DeleteLocalRef(string);
    }
    return stringArray;
}
jni_int Java_com_example_noobsql_MainActivity_createTable(JNIEnv* env, jobject /* this */, jstring table) {
    const char* table_name = env->GetStringUTFChars(table, nullptr);
    try {
        db->createTable(table_name);
        LOGV("TABLE::SUCCESS", "NEW TABLE CREATED");
    } catch (std::exception& e) {
        LOGV("TABLE::ERROR", "%s", e.what());
        return -1;
    }
    return 0;
}
jni_int Java_com_example_noobsql_MainActivity_deleteTable(JNIEnv* env, jobject /* this */, jstring table) {
    const char* table_name = env->GetStringUTFChars(table, nullptr);
    try {
        db->deleteTable(table_name);
        LOGV("TABLE::SUCCESS", "TABLE DELETED");
    } catch (std::exception& e) {
        LOGV("TABLE::ERROR", "%s", e.what());
        return -1;
    }
    return 0;
}

jni_int Java_com_example_noobsql_MainActivity_deleteRow(JNIEnv* env, jobject /* this */, jstring table, jstring edit1, jstring edit2) {
    const char* table_name = env->GetStringUTFChars(table, nullptr);
    const char* edit_1 = env->GetStringUTFChars(edit1, nullptr);
    const char* edit_2 = env->GetStringUTFChars(edit2, nullptr);
    try {
        db->deleteRow(table_name, edit_1, edit_2);
        LOGV("DELETE_ROW::SUCCESS", "ROW DELETED");
    } catch (std::exception& e) {
        LOGV("DELETE_ROW::ERROR", "%s", e.what());
        return -1;
    }
    return 0;
}
jni_int Java_com_example_noobsql_MainActivity_insertRow(JNIEnv* env, jobject /* this */, jstring table, jobjectArray edit1, jobjectArray edit2) {
    const char* table_name = env->GetStringUTFChars(table, nullptr);
    std::vector<std::string> edit_1 = jobjectArrayToVector(env, edit1);
    std::vector<std::string> edit_2 = jobjectArrayToVector(env, edit2);
    try {
        db->insertRow(table_name, edit_1, edit_2);
        LOGV("INSERT_ROW::SUCCESS", "ROW ADDED");
    } catch (std::exception& e) {
        LOGV("INSERT_ROW::ERROR", "%s", e.what());
        return -1;
    }
    return 0;
}
jni_object Java_com_example_noobsql_MainActivity_selectRow(JNIEnv* env, jobject /* this */, jstring table, jstring edit1, jstring edit2) {
    jclass stringClass = env->FindClass("java/lang/String");
    jobjectArray stringArrayArray;
    try {
        const char* table_name = env->GetStringUTFChars(table, nullptr);
        const char* edit_1 = env->GetStringUTFChars(edit1, nullptr);
        const char* edit_2 = env->GetStringUTFChars(edit2, nullptr);
        std::vector<std::vector<std::string>> rows = db->selectRow(table_name, edit_1, edit_2);

        if (rows.size() > 0) {
            for (int i = 0; i < rows.size(); i++) {
                jobjectArray stringArray = env->NewObjectArray(rows[i].size(), stringClass, nullptr);
                if (i == 0) stringArrayArray = env->NewObjectArray(rows.size(), env->GetObjectClass(stringArray), nullptr);
                for (int j = 0; j < rows[i].size(); j++) {
                    jstring string = env->NewStringUTF(rows[i][j].c_str());
                    env->SetObjectArrayElement(stringArray, j, string);
                    env->DeleteLocalRef(string);
                }
                env->SetObjectArrayElement(stringArrayArray, i, stringArray);
                env->DeleteLocalRef(stringArray);
            }
            LOGV("SELECT_ROW::SUCCESS", "ROWS SELECTED");
            return stringArrayArray;
        }
    } catch (std::exception& e) {
        LOGV("SELECT_ROW::ERROR", "%s", e.what());
    }
    jobjectArray stringArray = env->NewObjectArray(0, stringClass, nullptr);
    stringArrayArray = env->NewObjectArray(0, env->GetObjectClass(stringArray), nullptr);
    return stringArrayArray;
}
jni_int Java_com_example_noobsql_MainActivity_updateRow(JNIEnv* env, jobject /* this */, jstring table,
                                                        jobjectArray edit1, jobjectArray edit2, jstring edit3, jstring edit4) {
    const char* table_name = env->GetStringUTFChars(table, nullptr);
    std::vector<std::string> edit_1 = jobjectArrayToVector(env, edit1);
    std::vector<std::string> edit_2 = jobjectArrayToVector(env, edit2);
    const char* edit_3 = env->GetStringUTFChars(edit3, nullptr);
    const char* edit_4 = env->GetStringUTFChars(edit4, nullptr);
    try {
        db->updateRow(table_name, edit_1, edit_2, edit_3, edit_4);
        LOGV("UPDATE_ROW::SUCCESS", "ROW UPDATED");
    } catch (std::exception& e) {
        LOGV("UPDATE_ROW::ERROR", "%s", e.what());
        return -1;
    }
    return 0;
}

jni_int Java_com_example_noobsql_MainActivity_addColumn(JNIEnv* env, jobject /* this */, jstring table, jstring edit1) {
    const char* table_name = env->GetStringUTFChars(table, nullptr);
    const char* edit_1 = env->GetStringUTFChars(edit1, nullptr);
    try {
        db->addColumn(table_name, edit_1);
        LOGV("ADD_COLUMN::SUCCESS", "COLUMN ADDED");
    } catch (std::exception& e) {
        LOGV("ADD_COLUMN::ERROR", "%s", e.what());
        return -1;
    }
    return 0;
}
jni_int Java_com_example_noobsql_MainActivity_deleteColumn(JNIEnv* env, jobject /* this */, jstring table, jstring edit1) {
    const char* table_name = env->GetStringUTFChars(table, nullptr);
    const char* edit_1 = env->GetStringUTFChars(edit1, nullptr);
    try {
        db->deleteColumn(table_name, edit_1);
        LOGV("DELETE_COLUMN::SUCCESS", "COLUMN DELETED");
    } catch (std::exception& e) {
        LOGV("DELETE_COLUMN::ERROR", "%s", e.what());
        return -1;
    }
    return 0;
}
jni_int Java_com_example_noobsql_MainActivity_renameColumn(JNIEnv* env, jobject /* this */, jstring table, jstring edit1, jstring edit2) {
    const char* table_name = env->GetStringUTFChars(table, nullptr);
    const char* edit_1 = env->GetStringUTFChars(edit1, nullptr);
    const char* edit_2 = env->GetStringUTFChars(edit2, nullptr);
    try {
        db->renameColumn(table_name, edit_1, edit_2);
        LOGV("RENAME_COLUMN::SUCCESS", "COLUMN RENAMED");
    } catch (std::exception& e) {
        LOGV("RENAME_COLUMN::ERROR", "%s", e.what());
        return -1;
    }
    return 0;
}

jni_object_array Java_com_example_noobsql_MainActivity_getColumns(JNIEnv* env, jobject /* this */, jstring table) {
    try {
        const char* table_name = env->GetStringUTFChars(table, nullptr);
        std::vector<std::string> columns = db->getColumns(table_name);

        jclass stringClass = env->FindClass("java/lang/String");
        jobjectArray stringArray = env->NewObjectArray(columns.size(), stringClass, nullptr);
        for (int i = 0; i < columns.size(); i++) {
            jstring string = env->NewStringUTF(columns[i].c_str());
            env->SetObjectArrayElement(stringArray, i, string);
            env->DeleteLocalRef(string);
        }
        LOGV("GET_COLUMNS::SUCCESS", "RETURNED COLUMNS");
        return stringArray;
    } catch (std::exception& e) {
        LOGV("GET_COLUMNS::ERROR", "%s", e.what());
    }
    return NULL;
}