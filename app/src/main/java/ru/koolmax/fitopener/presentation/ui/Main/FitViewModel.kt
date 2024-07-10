package ru.koolmax.fitopener.presentation.ui.Main

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ru.koolmax.fitopener.data.DeviceFile
import ru.koolmax.fitopener.data.HeartZoneImpl
import ru.koolmax.fitopener.data.HeartZoneInfo
import ru.koolmax.fitopener.data.OnProgressListener
import ru.koolmax.fitopener.data.ble.DeviceRepository
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.data.db.FitSessionItem
import ru.koolmax.fitopener.data.media.FileRepository
import kotlinx.coroutines.launch

class FitViewModel(private val context: Context,
                   private val fileRepository: FileRepository,
                   private val fitRepository: FitRepository,
                   private val bleRepository: DeviceRepository
) : ViewModel() {
    val fitSessionList = MutableLiveData<List<FitSessionItem>>()
    val heartZone = MutableLiveData<List<Pair<HeartZoneInfo, Int>>>()
    val deviceFileList = MutableLiveData<List<DeviceFile>>()

    fun addFitToLib(uri: Uri) {
        viewModelScope.launch {
            fileRepository.addFit(uri, context)?.let {
                fitRepository.add(it)
                fitSessionList.postValue(fitRepository.all())
            }
        }
    }

    fun getSessionList() {
        viewModelScope.launch {
            fitSessionList.value = fitRepository.all()
        }
    }

    fun getFitSession(fit: String) : LiveData<FitSessionItem> {
        val result = MutableLiveData<FitSessionItem>()
        viewModelScope.launch {
            result.postValue(fitRepository.getSession(fit))
            fileRepository.getRecords(fit, context)?.let {
                heartZone.value = it.getHeartZone(HeartZoneImpl).reversed()
            }
        }
        return result
    }

    fun setDisplayed(fit: String) {
        viewModelScope.launch {
            fitRepository.getSession(fit).let {
                it.displayed = 1
                fitRepository.update(it)
                fitSessionList.postValue(fitRepository.all())
            }
        }
    }

    fun delFitSession(fit: String) {
        viewModelScope.launch {
            fileRepository.delFit(fit, context)
            fitRepository.delete(fit)
            fitSessionList.postValue(fitRepository.all())
        }
    }

    @SuppressLint("MissingPermission")
    fun connect(address: String, progressListener: OnProgressListener) {
        viewModelScope.launch {
            bleRepository.connect(address, context, viewModelScope, progressListener)
            deviceFileList.postValue(bleRepository.getFileList())
        }
    }

    @SuppressLint("MissingPermission")
    fun SaveDeviceFileToLib(file: String) {
        viewModelScope.launch {
            val data = bleRepository.getFile(file)
            fileRepository.addFit(data, file, context)?.let {
                fitRepository.add(it)
                fitSessionList.postValue(fitRepository.all())
            }
            //fileRepository
            //Log.i("FitOpener1", "size file ${s.size}")
            //Log.i("FitOpener1", "begin file ${s.toList().subList(0, 20).toByteArray().toHex()}")
            //Log.i("FitOpener1", "end file ${s.toList().subList(s.size-21, s.size).toByteArray().toHex()}")
        }
    }
}

class FitViewModelFactory(private val context: Context,
                          private val fileRepository: FileRepository,
                          private val fitRepository: FitRepository,
                          private val bleRepository: DeviceRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FitViewModel(context, fileRepository, fitRepository, bleRepository) as T
    }
}