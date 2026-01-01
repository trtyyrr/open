package com.stealth.manager.modules

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

class ModuleManager {

    /**
     * 安全地解压 Zip 文件到指定目录
     */
    fun unzipFile(zipFile: File, targetDir: File) {
        if (!zipFile.exists()) return
        if (!targetDir.exists()) targetDir.mkdirs()

        ZipInputStream(zipFile.inputStream().buffered()).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                val outFile = File(targetDir, entry.name)
                if (entry.isDirectory) {
                    outFile.mkdirs()
                } else {
                    outFile.parentFile?.mkdirs()
                    FileOutputStream(outFile).use { output ->
                        zip.copyTo(output)
                    }
                }
                zip.closeEntry()
                entry = zip.nextEntry
            }
        }
    }

    fun init() {
        // 这里的逻辑可以根据你的需求编写
        println("Stealth Module Manager Initialized")
    }
}