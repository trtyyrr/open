package com.stealth.manager.modules

import android.content.Context

// 定义模块数据结构
data class StealthModule(
    val name: String,
    val description: String,
    var isEnabled: Boolean = false
)

class ModuleManager(private val context: Context) {
    fun listModules(): List<StealthModule> {
        val list = mutableListOf<StealthModule>()
        try {
            // 扫描 assets/kernel_patches 下的所有文件夹
            val patches = context.assets.list("kernel_patches")
            patches?.forEach { folderName ->
                // 只有非空文件夹（Git里有文件的）才会被扫描到
                list.add(StealthModule(folderName, "来自本地 Assets 的补丁脚本"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}