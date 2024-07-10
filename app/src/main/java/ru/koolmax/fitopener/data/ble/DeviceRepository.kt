package ru.koolmax.fitopener.data.ble

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import ru.koolmax.fitopener.data.DeviceFile
import ru.koolmax.fitopener.data.OnProgressListener
import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.android.kotlin.ble.client.main.callback.ClientBleGatt

class DeviceRepository() {
    private lateinit var address: String
    private var btGatt: ClientBleGatt? = null
    //private var services: ClientBleGattServices? = null
    private var modem: YModem? = null

    @SuppressLint("MissingPermission")
    suspend fun connect(address: String, context: Context, scope: CoroutineScope, progressListener: OnProgressListener) {
        this.address = address
        btGatt = ClientBleGatt.connect(context, address, scope)
        modem = YModem(btGatt?.discoverServices()!!, scope, progressListener).apply { start() }
        Log.i("FitOpener1", "connect")
    }

    @SuppressLint("MissingPermission")
    suspend fun getFileList() : List<DeviceFile> {
        val list = mutableListOf<DeviceFile>()
//?        val list = mutableListOf<FitFileRecord>(FitFileRecord("20220618070720.fit", 83081),
//?            FitFileRecord("20240510133515.fit", 183081)  )
        modem?.readFile("filelist.txt")?.decodeToString()?.split("\r\n")?.forEach {
            val fields = it.split(" ")
            try {
                list.add(DeviceFile(fields[0], fields[1].toInt()))
            } catch (e: Exception) {
                //Log.i("FitOpener1", "exeption ${fields.toString()}")
            }
        }
        return list
    }

    @SuppressLint("MissingPermission")
    suspend fun getFile(file: String): ByteArray {
//?        return byteArrayOf(0x22, 0x22,0x22,0x22,0x22)
        return modem?.readFile(file)!!
    }
}

//        services?.let {
//            it.services.forEach {
//                //Log.i("FitOpener1", "service ${it.uuid}")
//                it.characteristics.forEach {
//                    //Log.i("FitOpener1", "characteristic ${it.uuid}")
//                    it.properties.forEach {
//                        //Log.i("FitOpener1", "property ${it.name}")
//                    }
//                }
//            }
//                val serviceUUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")
//                val charUUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")
//                it.findService(serviceUUID)?.let {
//                    it.findCharacteristic(charUUID)?.read().let {
//                        Log.i("FitOpener1", "value ${it.toString()}")
//                    }
//                }
