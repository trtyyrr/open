package com.stealth.manager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 修复 packageManager 引用
        val packageName = this.packageName
        val version = try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: Exception) {
            "1.0.0"
        }

        // 移除未定义的 NativeLib 引用，改为简单的日志或提示
        Toast.makeText(this, "Stealth Manager Version: $version", Toast.LENGTH_SHORT).show()
        
        // 如果你需要在这里做认证逻辑，可以在此扩展
    }
}