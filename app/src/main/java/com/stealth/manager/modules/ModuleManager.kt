package com.stealth.manager.modules

import android.content.Context

// 必须定义这个类，否则 Adapter 会报错
data class StealthModule(
    val name: String,
    val description: String,
    var isEnabled: Boolean = false
)

class ModuleManager(private val context: Context) {
    fun listModules(): List<StealthModule> {
        val list = mutableListOf<StealthModule>()
        // 模拟扫描 assets
        context.assets.list("kernel_patches")?.forEach {
            list.add(StealthModule(it, "内核补丁模块"))
        }
        return list
    }
}