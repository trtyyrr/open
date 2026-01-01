// 目录标记：app/src/main/java/com/stealth/manager/AuthActivity.kt
fun grantRoot(targetPackage: String) {
    val uid = packageManager.getPackageInfo(targetPackage, 0).applicationInfo.uid
    
    // 调用 JNI 接口，向内核发送“授权指令”
    // NativeLib.authorizeUid(0x7777, uid)
    NativeLib.sendAuthToKernel(0x1337, 0x7777, uid)
}