package ru.koolmax.fitopener.data.ble

import android.annotation.SuppressLint
import android.util.Log
import ru.koolmax.fitopener.data.OnProgressListener
import ru.koolmax.fitopener.domain.toHex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import no.nordicsemi.android.common.core.DataByteArray
import no.nordicsemi.android.kotlin.ble.client.main.service.ClientBleGattServices
import no.nordicsemi.android.kotlin.ble.core.data.BleWriteType
import java.util.UUID

class YModem(private val services: ClientBleGattServices, private val scope: CoroutineScope, private val progressListener: OnProgressListener) {
    private var curWaitCmd: ReceiveFileCmd? = null
    private var curFileSizeRequest: FileSizeRequest? = null
    private var curFileRequest: FileRequest? = null

    suspend fun start() {
        subscribeNotifications()
    }

    suspend fun readFile(file: String): ByteArray {
        var data = byteArrayOf()
        curWaitCmd = ReceiveFileCmd(file)
        curWaitCmd?.let {
            sendCmd(it.getData())
            it.wait()
            curWaitCmd = null
            Log.i("FitOpener1", "end cmd wait")
            curFileSizeRequest = FileSizeRequest()
            curFileSizeRequest?.let {
                sendTX(it.getData())
                it.wait()
                sendACK()
                val size = it.size
                progressListener.onBegin(size)
                curFileSizeRequest = null
                Log.i("FitOpener1", "end size wait ${size}")

                curFileRequest = FileRequest(size)
                curFileRequest?.let {
                    sendTX(it.getData())
                    it.wait()
                    Log.i("FitOpener1", "end file wait ${it.getFileData().toHex()}")
                    data = it.getFileData()
                    progressListener.onFinish()
                    curFileRequest = null
                }
            }
        }
        return data
    }

    private suspend fun sendACK() {
        sendTX(byteArrayOf(0x06))
    }

    @SuppressLint("MissingPermission")
    private suspend fun sendCmd(data: ByteArray) {
        val serviceUUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
        val charCTRLUUID = UUID.fromString("6e400004-b5a3-f393-e0a9-e50e24dcca9e")
        services.findService(serviceUUID)?.let {
            it.findCharacteristic(charCTRLUUID)?.splitWrite(DataByteArray(data))
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun sendTX(data: ByteArray) {
        val serviceUUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
        val charTXUUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")
        services.findService(serviceUUID)?.let {
            val startData = DataByteArray(data)
            it.findCharacteristic(charTXUUID)?.splitWrite(startData, BleWriteType.NO_RESPONSE)
        }
    }

    private suspend fun subscribeNotifications() {
        val serviceUUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
        val charRXUUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")
        val charCTRLUUID = UUID.fromString("6e400004-b5a3-f393-e0a9-e50e24dcca9e")

        services.findService(serviceUUID)?.let {
            it.findCharacteristic(charRXUUID)?.getNotifications()?.onStart {
                //Log.i("FitOpener1", "on start RX notify")
            }?.onCompletion {
                //Log.i("FitOpener1", "on end RX notify")
            }?.onEach {
                //Log.i("FitOpener1", "on RX notify ${it.value.toHex()}")
                curFileSizeRequest?.receive(it.value)
                curFileRequest?.let { request ->
                    if(request.receive(it.value))
                        sendACK()
                    progressListener.onStep(request.receivedSize())
                }
            }?.onEmpty {
                //Log.i("FitOpener1", "on empty notify")
            }?.launchIn(scope)

            it.findCharacteristic(charCTRLUUID)?.getNotifications()?.onEach {
                //Log.i("FitOpener1", "on CMD notify ${it.value.toHex()}")
                curWaitCmd?.receive(it.value)
            }?.launchIn(scope)
        }
        delay(500)
    }
}

private fun getCRC(buf: List<Byte>): Byte {
    return 0x00
}

class ReceiveFileCmd(private val file: String) {
    private val mutex = Mutex(true)

    fun getData(): ByteArray
    {
        val requestBuf = file.toByteArray().toMutableList()
        requestBuf.add(0, 0x05)
        requestBuf.add(0x57)
        //requestBuf.add(getCRC(requestBuf))
        return requestBuf.toByteArray()
    }

    suspend fun wait() {
        mutex.withLock {  }
    }

    fun receive(buf: ByteArray) {
        mutex.unlock()
    }
}

class FileSizeRequest {
    private val mutex = Mutex(true)
    var size = 0

    fun getData() = byteArrayOf(0x43)

    suspend fun wait() {
        mutex.withLock {  }
    }

    fun receive(buf: ByteArray) {
        decode(buf)
        mutex.unlock()
    }

    private fun decode(buf: ByteArray) {
        val lines = buf.decodeToString(3, buf.size-2).split(" ")
        size = lines[1].trimEnd(0.toChar()).toInt()
    }
}

class FileRequest(private val size: Int) {
    private val mutex = Mutex(true)
    private val buffer = mutableListOf<Byte>()
    private val buffer1K = mutableListOf<Byte>()

    fun getData() = byteArrayOf(0x43)

    fun getFileData() = buffer.subList(0, size).toByteArray()

    suspend fun wait() {
        mutex.withLock {  }
    }

    fun receive(buf: ByteArray): Boolean {
        //Log.i("FitOpener1", "reseive buf len=${buf.size}  ${buf.toHex()}")
        var needTX = false
        buffer1K.addAll(buf.toList())
        if(receivedSize() < size+5) {
            if(buffer1K.size >= 1024+5) {
                buffer.addAll(extractDataFrom1K())
                buffer1K.clear()
                needTX = true
            }
        } else {
            Log.i("FitOpener1", "buff ${buffer.size}")
            Log.i("FitOpener1", "buff1K ${extractDataFrom1K().toByteArray().toHex()}")
            buffer.addAll(extractDataFrom1K())
            buffer1K.clear()
            mutex.unlock()
        }
        //Log.i("FitOpener1", "byff 1K ${buffer1K.size}")
        //Log.i("FitOpener1", "byff ${buffer.size}")
        return needTX
    }

    private fun extractDataFrom1K(): List<Byte> {
        return buffer1K.subList(3, buffer1K.size-2)
    }

    fun receivedSize() = buffer.size + buffer1K.size
}
