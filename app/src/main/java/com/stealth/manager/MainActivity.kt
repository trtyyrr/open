package com.stealth.manager

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stealth.manager.modules.ModuleAdapter
import com.stealth.manager.modules.ModuleManager
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var tvLog: TextView
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 初始化 UI 组件
        tvLog = findViewById(R.id.tv_log)
        tvStatus = findViewById(R.id.tv_engine_status) // 对应你截图顶部的文字
        val btnSelect = findViewById<Button>(R.id.btn_select)
        val btnPatch = findViewById<Button>(R.id.btn_patch)
        val rvModules = findViewById<RecyclerView>(R.id.rv_modules)

        // 2. 检查授权状态 (Root 检查)
        checkRootStatus()

        // 3. 加载并显示模块列表
        val moduleManager = ModuleManager(this)
        val modules = moduleManager.listModules()
        
        rvModules.layoutManager = LinearLayoutManager(this)
        rvModules.adapter = ModuleAdapter(modules)

        if (modules.isEmpty()) {
            printLog("未检测到模块，请确保 assets/kernel_patches 下有文件夹且内含文件")
        } else {
            printLog("成功加载 ${modules.size} 个补丁模块")
        }

        // 4. 按钮事件
        btnSelect.setOnClickListener {
            printLog("正在调用系统文件选择器...")
        }

        btnPatch.setOnClickListener {
            val activeCount = modules.count { it.isEnabled }
            printLog("开始执行任务：共勾选 $activeCount 个补丁")
            // 这里可以开始你的 magiskboot 链式调用逻辑
        }
    }

    private fun checkRootStatus() {
        Thread {
            val hasRoot = try {
                val p = Runtime.getRuntime().exec("su")
                p.outputStream.write("exit\n".toByteArray())
                p.outputStream.flush()
                p.waitFor() == 0
            } catch (e: Exception) { false }

            runOnUiThread {
                if (hasRoot) {
                    tvStatus.text = "授权状态：已获得 Root 权限"
                    tvStatus.setTextColor(Color.GREEN)
                } else {
                    tvStatus.text = "授权状态：未授权 (仅限无根修补)"
                    tvStatus.setTextColor(Color.YELLOW)
                }
            }
        }.start()
    }

    private fun printLog(msg: String) {
        tvLog.append("\n[${System.currentTimeMillis() % 10000}] $msg")
    }
}