package ru.koolmax.fitopener.presentation.ui.Main.DeviceList

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.android.kotlin.ble.core.ServerDevice
import no.nordicsemi.android.kotlin.ble.scanner.BleScanner
import no.nordicsemi.android.kotlin.ble.scanner.aggregator.BleScanResultAggregator

class DeviceListViewModel(private val context: Context) : ViewModel() {

    val deviceList = MutableLiveData<List<ServerDevice>>()

    @SuppressLint("MissingPermission")
    fun startScan() {
        val aggregator = BleScanResultAggregator()
        BleScanner(context).scan()
            .map { aggregator.aggregateDevices(it) } //Add new device and return an aggregated list
            .onEach { deviceList.postValue(it) } //Propagated state to UI
            .launchIn(viewModelScope) //Scanning will stop after we leave the screen
    }
}

class DeviceListViewModelFactory(private val context: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DeviceListViewModel(context) as T
    }
}