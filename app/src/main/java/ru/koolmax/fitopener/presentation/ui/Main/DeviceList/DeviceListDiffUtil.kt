package ru.koolmax.fitopener.presentation.ui.Main.DeviceList

import androidx.recyclerview.widget.DiffUtil
import no.nordicsemi.android.kotlin.ble.core.ServerDevice

class DeviceListDiffUtil: DiffUtil.ItemCallback<ServerDevice>() {
    override fun areItemsTheSame(oldItem: ServerDevice, newItem: ServerDevice): Boolean {
        return oldItem.address == newItem.address
    }

    override fun areContentsTheSame(oldItem: ServerDevice, newItem: ServerDevice): Boolean {
        return oldItem == newItem
    }
}