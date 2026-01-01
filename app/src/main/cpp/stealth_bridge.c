// 目录标记：/app/src/main/cpp/stealth_bridge.c
#include <jni.h>
#include <sys/prctl.h>
#include <unistd.h>

/* 内核补丁中预设的提权暗号 */
#define STEALTH_SECRET_OPTION 0x1337
#define STEALTH_SECRET_KEY    0xC0DE

JNIEXPORT jint JNICALL
Java_com_stealth_manager_NativeLib_requestRoot(JNIEnv *env, jobject thiz) {
    // 执行被 patch 后的 prctl 系统调用
    // 如果内核补丁匹配成功，当前进程会被赋予 UID 0
    prctl(STEALTH_SECRET_OPTION, STEALTH_SECRET_KEY, 0, 0, 0);
    
    return getuid(); // 成功返回 0，失败返回原 UID
}
