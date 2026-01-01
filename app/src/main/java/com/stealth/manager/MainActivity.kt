package com.stealth.manager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stealth.manager.utils.BinaryHelper
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 启动时初始化二进制环境
        thread {
            val binPath = BinaryHelper.initMagiskboot(this)
            
            runOnUiThread {
                if (binPath != null) {
                    Toast.makeText(this, "内核修补引擎已就绪", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "初始化失败，请检查架构兼容性", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}