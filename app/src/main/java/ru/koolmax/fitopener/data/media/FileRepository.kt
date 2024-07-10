package ru.koolmax.fitopener.data.media

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import ru.koolmax.fitopener.data.db.FitSessionItem
import com.garmin.fit.Decode
import com.garmin.fit.FitRuntimeException
import com.garmin.fit.MesgBroadcaster
import com.garmin.fit.SessionMesgListener
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

fun Uri.getName(context: Context): String {
    val returnCursor = context.contentResolver.query(this, null, null, null, null)
    val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val fileName = returnCursor.getString(nameIndex)
    returnCursor.close()
    return fileName
}

class FileRepository {
    //private var fitFileList = mutableListOf<IFitFile>()
    //private var fitFile: IFitFile? = null

    //fun GetFitList(context: Context): List<IFitFile> {
    //    if(fitFileList.size==0) {
//            for (file in context!!.filesDir.listFiles()) {
//                val fitFile = FitFileImpl(file)
//                if (fitFile.GetSession() != null)
//                    fitFileList.add(fitFile)
//            }
//        }
//        return fitFileList
//    }

    fun getRecords(name: String, context: Context): FitItem? {
        val file = File(context.filesDir, name)
        val stream = file.inputStream()
        stream.use {
            val records = FitItem()
            val decode = Decode()
            val broadcast = MesgBroadcaster(decode)
            broadcast.addListener(records)
            if (!decode.read(it, broadcast))
                return null
            records.endLoad()
            return records
        }
    }

    fun getSession(stream: InputStream, name: String): FitSessionItem? {
        try {
            val session = FitSessionItem(name, 0)
            stream.use {
                val decode = Decode()
                val broadcast = MesgBroadcaster(decode)
                //broadcast.addListener(session as FileIdMesgListener)
                broadcast.addListener(session as SessionMesgListener)
                if (!decode.read(it, broadcast))
                    return null
            }
            return session
        } catch (ex: FitRuntimeException) {
            return null
        }
    }

    fun addFit(uri: Uri, context: Context): FitSessionItem? {
        try {
                //for (fit in fitFileList) {
                //    if(fit.GetFile().name  == uri.getName(application!!)) return null
                //}
                var session: FitSessionItem? = null
                context.contentResolver.openInputStream(uri)?.let {
                    getSession(it, uri.getName(context))?.let {
                        session = it
                    }
                }

                context.contentResolver.openInputStream(uri)?.let {
                    val outputFile = File(context.filesDir, uri.getName(context))
                    val outputStream = outputFile.outputStream()

                    it.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                    return session
                }
            return null
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun addFit(data: ByteArray, fileName: String, context: Context): FitSessionItem? {
        try {
            //for (fit in fitFileList) {
            //    if(fit.GetFile().name  == uri.getName(application!!)) return null
            //}
            val inputStream = ByteArrayInputStream(data)
            val session = getSession(inputStream, fileName)
            //if(session!=null) {
                val outputFile = File(context.filesDir, fileName)
                val outputStream = outputFile.outputStream()

                ByteArrayInputStream(data).use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
            //}
            return session
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun delFit(name: String, context: Context) {
        val file = File(context.filesDir, name)
        file.delete()
    }
}