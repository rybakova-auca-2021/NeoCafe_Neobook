package com.example.neocafe.constants

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object LocalStorageProvider {
    fun getFile(context: Context, uri: Uri): File? {
        val fileName = "temp_image.jpg"
        val file = File(context.filesDir, fileName)

        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(4 * 1024) // 4k buffer
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                return file
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}


