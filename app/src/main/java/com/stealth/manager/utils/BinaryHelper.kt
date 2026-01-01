package com.stealth.manager.utils

import android.content.Context
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object BinaryHelper {
    private const val TAG = "StealthBinary"

    /**
     * 自动提取并准备 magiskboot 执行环境
     * @return 提取后的 magiskboot 绝对路径，失败则返回 null
     */
    fun initMagiskboot(context: Context): String? {
        val internalDir = context.filesDir
        val targetFile = File(internalDir, "magiskboot")
        
        // 1. 获取当前设备的首选架构 (arm64-v8a 或 armeabi-v7a)
        val abi = Build.SUPPORTED_ABIS[0]
        val assetPath = "bin/$abi/magiskboot"

        try {
            // 2. 检查 assets 中是否存在对应架构的文件
            val assetExists = context.assets.list("bin/$abi")?.contains("magiskboot") ?: false
            if (!assetExists) {
                Log.e(TAG, "Assets 中找不到架构为 $abi 的二进制文件")
                return null
            }

            // 3. 开始拷贝文件
            context.assets.open(assetPath).use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }

            // 4. 关键步骤：赋予可执行权限
            // 等同于 shell 中的 chmod +x 或 chmod 755
            targetFile.setReadable(true, false)
            targetFile.setExecutable(true, false)
            targetFile.setWritable(true)

            Log.i(TAG, "Magiskboot 成功安装至: ${targetFile.absolutePath}")
            return targetFile.absolutePath

        } catch (e: Exception) {
            Log.e(TAG, "安装失败: ${e.message}")
            e.printStackTrace()
            return null
        }
    }
}