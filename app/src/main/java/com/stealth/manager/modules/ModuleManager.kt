// 目录标记：/app/src/main/java/com/stealth/manager/modules/ModuleManager.kt
package com.stealth.manager.modules

import java.io.File

object ModuleManager {
    private const val MODULE_PATH = "/data/adb/stealth/modules"

    /**
     * 安装模块：解压 ZIP 并设置权限
     */
    fun installModule(zipFile: File) {
        val modId = "mod_${System.currentTimeMillis()}"
        val targetDir = File(MODULE_PATH, modId)
        
        // 1. 调用解压工具 (此处省略 unzip 逻辑)
        unzip(zipFile, targetDir)
        
        // 2. 强制设置权限，否则 OverlayFS 无法加载
        Runtime.getRuntime().exec("chmod -R 755 ${targetDir.absolutePath}")
        Runtime.getRuntime().exec("chown -R root:root ${targetDir.absolutePath}")
    }

    /**
     * 禁用/启用模块
     */
    fun toggleModule(modId: String, enable: Boolean) {
        val disableFile = File("$MODULE_PATH/$modId/disable")
        if (enable) {
            if (disableFile.exists()) disableFile.delete()
        } else {
            disableFile.createNewFile()
        }
    }
}