package ru.koolmax.fitopener.presentation.ui.Main.DeviceList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.koolmax.fitopener.R
import no.nordicsemi.android.kotlin.ble.core.ServerDevice

class DeviceDataAdapter: ListAdapter<ServerDevice, DeviceDataAdapter.Holder>(DeviceListDiffUtil()) {

    var onItemClickListener: ((ServerDevice) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val itm = getItem(position)
        holder.setData(itm)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(itm)
        }
    }

    class Holder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val nameView = view.findViewById<TextView>(R.id.name_view)
        private val addressView = view.findViewById<TextView>(R.id.address_view)
        private val levelView = view.findViewById<TextView>(R.id.level_view)

        fun setData(itm: ServerDevice) {
            addressView.text = itm.address
            nameView.text = itm.name ?: "<неизвестное устройство>"
            //levelView.text = itm.rssi.toString()
        }
    }
}