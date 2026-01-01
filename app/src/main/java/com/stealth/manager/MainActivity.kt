// 目录标记：/app/src/main/java/com/stealth/manager/MainActivity.kt
package com.stealth.manager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.stealth.manager.utils.BinaryHelper
import java.io.File
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var logView: TextView
    private lateinit var patchBtn: Button
    private var magiskbootPath: String? = null
    private var selectedBootUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logView = findViewById(R.id.log_output)
        patchBtn = findViewById(R.id.btn_start_patch)
        val selectBtn = findViewById<Button>(R.id.btn_select_boot)
        val statusText = findViewById<TextView>(R.id.bin_status)

        // 1. 初始化引擎 (从 Assets 提取二进制)
        thread {
            magiskbootPath = BinaryHelper.initMagiskboot(this)
            runOnUiThread {
                if (magiskbootPath != null) {
                    statusText.text = "引擎状态: 就绪 (Path: $magiskbootPath)"
                    appendLog("系统架构检测成功，二进制引擎已提取。")
                } else {
                    statusText.text = "引擎状态: 初始化失败"
                    appendLog("错误: 无法提取二进制文件，请检查架构。")
                }
            }
        }

        // 2. 选择文件
        selectBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "*/*" }
            startActivityForResult(intent, 1001)
        }

        // 3. 开始修补
        patchBtn.setOnClickListener {
            if (selectedBootUri != null && magiskbootPath != null) {
                executeStealthPatch()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            selectedBootUri = data?.data
            appendLog("已选择镜像: ${selectedBootUri?.path}")
            patchBtn.isEnabled = true
        }
    }

    private fun executeStealthPatch() {
        thread {
            val workDir = File(filesDir, "work").apply { if (exists()) deleteRecursively(); mkdirs() }
            val bootFile = File(workDir, "boot.img")
            
            appendLog("正在准备工作区...")
            // 将选中的 Uri 内容拷贝到工作区
            contentResolver.openInputStream(selectedBootUri!!)?.use { input ->
                bootFile.outputStream().use { input.copyTo(it) }
            }

            appendLog("开始解压镜像 (unpack)...")
            runCmd("$magiskbootPath unpack ${bootFile.absolutePath}", workDir)

            appendLog("执行方案 4: 硬件特征欺骗 (HexPatch)...")
            val hexCmd = "$magiskbootPath hexpatch kernel " +
                    "616e64726f6964626f6f742e7665726966696564626f6f7473746174653d6f72616e6765 " +
                    "616e64726f6964626f6f742e7665726966696564626f6f7473746174653d677265656e20"
            runCmd(hexCmd, workDir)

            appendLog("重新封装镜像 (repack)...")
            runCmd("$magiskbootPath repack ${bootFile.absolutePath} new_boot.img", workDir)

            appendLog("修补完成！文件保存在: ${workDir.absolutePath}/new_boot.img")
            appendLog("提示: 请使用 fastboot flash boot 刷入此镜像。")
        }
    }

    private fun runCmd(cmd: String, dir: File) {
        try {
            val process = Runtime.getRuntime().exec(cmd, null, dir)
            val output = process.inputStream.bufferedReader().readText()
            val error = process.errorStream.bufferedReader().readText()
            if (output.isNotEmpty()) appendLog(output)
            if (error.isNotEmpty()) appendLog("Error: $error")
            process.waitFor()
        } catch (e: Exception) {
            appendLog("执行异常: ${e.message}")
        }
    }

    private fun appendLog(text: String) {
        runOnUiThread { logView.append("$text\n") }
    }
}