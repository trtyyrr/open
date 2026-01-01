package com.stealth.manager

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stealth.manager.modules.ModuleAdapter
import com.stealth.manager.modules.ModuleManager
import com.stealth.manager.modules.StealthModule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 绑定 UI (与 XML 里的 ID 严格对应)
        val tvLog = findViewById<TextView>(R.id.tv_log)
        val btnPatch = findViewById<Button>(R.id.btn_patch)
        val btnSelect = findViewById<Button>(R.id.btn_select)
        val ivAuthStatus = findViewById<ImageView>(R.id.iv_auth_status)
        val rvModules = findViewById<RecyclerView>(R.id.rv_modules)

        // 2. 初始化模块列表
        val moduleManager = ModuleManager(this)
        val modules = moduleManager.listModules()
        
        rvModules.layoutManager = LinearLayoutManager(this)
        rvModules.adapter = ModuleAdapter(modules)

        // 3. 按钮点击事件
        btnSelect.setOnClickListener {
            tvLog.append("\n[系统] 正在打开文件选择器...")
        }

        btnPatch.setOnClickListener {
            tvLog.append("\n[系统] 开始执行修补任务...")
        }
    }
}