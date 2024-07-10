package ru.koolmax.fitopener.data.media

import android.net.Uri
import androidx.core.net.toUri
import ru.koolmax.fitopener.domain.IDeviceDirectory
import java.io.File
import java.io.FilenameFilter
import java.util.Locale

object DeviceDirectoryImpl: IDeviceDirectory {
    override fun getListUri(): List<Uri> {
        var result = mutableListOf<Uri>()
        val dir = File(directory)
        if (dir.isDirectory) {
            val filter = FilenameFilter { d: File?, s: String -> s.lowercase(Locale.getDefault()).endsWith(".fit") }
            return dir.listFiles(filter).map { it.toUri() }
        }
        return result
    }

    override fun setDir(path: String) {
        directory = path
    }

    var directory: String = ""
}