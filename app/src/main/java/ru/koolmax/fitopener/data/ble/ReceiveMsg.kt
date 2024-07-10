package ru.koolmax.fitopener.data.ble

//enum class TypeRequest() { NONE, REQUEST_FILE_LIST, REQUEST_FILE };
//
//open class ReceiveMsg(val typeRequest: TypeRequest = TypeRequest.NONE) {
//    protected val buf = mutableListOf<Byte>()
//
//    open suspend fun add(bytes: ByteArray) {
//        buf.addAll(bytes.asIterable())
//        Log.i("FitOpener1", "buf ${buf.size}")
//    }
//
//    protected open fun isEnd(bytes: ByteArray): Boolean {
//        return bytes.size < 182 && buf.size > 5
//    }
//
//    fun received1K(): Boolean {
//        return buf.size % 1024 == 0
//    }
//}
//
//class FileListMsg(): ReceiveMsg(TypeRequest.REQUEST_FILE_LIST) {
//    private val mutext = Mutex()
//    var onDecode: ((List<FitFileRecord>) -> Unit)? = null
//
//    override suspend fun add(bytes: ByteArray) {
//        mutext.withLock {
//            super.add(bytes)
//            if (isEnd(bytes)) {
//                decode()
//            }
//        }
//    }
//
//    private fun decode() {
//        val list = mutableListOf<FitFileRecord>()
//        val text = buf.toByteArray().decodeToString(3, buf.size-2).split("\r\n")
//        text.forEach {
//            val fields = it.split(" ")
//            try {
//                list.add(FitFileRecord(fields[0], fields[1].toInt()))
//            } catch (e: Exception) {
//                Log.i("FitOpener1", "exeption ${fields.toString()}")
//            }
//        }
//        onDecode?.invoke(list)
//        buf.clear()
//    }
//}
//
//class FileMsg(): ReceiveMsg(TypeRequest.REQUEST_FILE) {
//    var onDecode: ((ByteArray) -> Unit)? = null
//
//    override suspend fun add(bytes: ByteArray) {
//        super.add(bytes)
//        if (isEnd(bytes)) {
//            decode()
//        }
//        if(buf.size==1029) {
//            val text = buf.toByteArray().toHex()
//            Log.i("FitOpener1", "finish $text")
//        }
//    }
//
//    override fun isEnd(bytes: ByteArray): Boolean {
//        return bytes.size >= 345629
//    }
//
//    private fun decode() {
//        val text = buf.toByteArray().toHex()
//        Log.i("FitOpener1", "finish $text")
////        val list = mutableListOf<FitFileRecord>()
////        repeat(3) {
////            buf.removeAt(0)
////        }
////        repeat(2) {
////            buf.removeLast()
////        }
////        val text = buf.toByteArray().decodeToString(3, buf.size-2).split("\r\n")
////        text.forEach {
////            val fields = it.split(" ")
////            try {
////                list.add(FitFileRecord(fields[0], fields[1].toInt()))
////            } catch (e: Exception) {
////                Log.i("FitOpener1", "exeption ${fields.toString()}")
////            }
////        }
////        onDecode?.invoke(list)
////        buf.clear()
//    }
//}