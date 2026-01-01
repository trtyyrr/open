// 目录标记：/app/src/main/java/com/stealth/manager/MainActivity.kt
package com.stealth.manager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 点击修补按钮触发
        fun onPatchClicked(originalBootPath: String) {
            val internalFiles = filesDir.absolutePath
            
            // 1. 释放 assets 中的 magiskboot 和脚本
            AssetUtils.copyAsset(this, "magiskboot", "$internalFiles/magiskboot")
            AssetUtils.copyAsset(this, "patch_engine.sh", "$internalFiles/patch_engine.sh")
            
            // 2. 赋予执行权限
            File("$internalFiles/magiskboot").setExecutable(true)
            
            // 3. 执行本地修补
            val cmd = "sh $internalFiles/patch_engine.sh $internalFiles $originalBootPath"
            Runtime.getRuntime().exec(cmd)
        }
    }
}