package ru.koolmax.fitopener.presentation.ui.Device

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.DeviceFile
import ru.koolmax.fitopener.data.OnProgressListener
import ru.koolmax.fitopener.data.db.FitSessionItem

data class DeviceFileAdapterItem(val deviceFile: DeviceFile,
                                 val session: FitSessionItem?,
                                 var state: FitFileDataAdapter.StateItem = FitFileDataAdapter.StateItem.DEFAULT,
                                 var byteCount: Int=0)

class FitFileDataAdapter(): ListAdapter<DeviceFileAdapterItem, FitFileDataAdapter.FileHolder>(FitFileListDiffUtil()),
    OnProgressListener {
    enum class StateItem { DEFAULT, LOADING, DISABLED }

    var onItemClickListener: ((DeviceFileAdapterItem) -> Unit)? = null

    var curLoadingPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        when(StateItem.entries[viewType]) {
            StateItem.DEFAULT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device_file, parent, false)
                return FileHolder(view)
            }
            StateItem.DISABLED -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device_file_inactive, parent, false)
                return FileHolder(view)
            }
            StateItem.LOADING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device_file_loading, parent, false)
                return FileLoadingHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        val itm = getItem(position)
        holder.setData(itm)
        holder.itemView.setOnLongClickListener {
            onItemClickListener?.invoke(itm)
            return@setOnLongClickListener true
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itm = getItem(position)
        return itm.state.ordinal
    }

    override fun onViewRecycled(holder: FileHolder) {
        super.onViewRecycled(holder)
    }

    fun startLoadFile(file: String) {
        curLoadingPosition = currentList.indexOfFirst { it.deviceFile.file==file }
    }

    override fun onBegin(max: Int) {
        if(curLoadingPosition!=-1) {
            currentList.forEach { it.state = StateItem.DISABLED }
            currentList[curLoadingPosition].state = StateItem.LOADING
            notifyItemRangeChanged(0, currentList.size)
        }
    }

    override fun onStep(count: Int) {
        if(curLoadingPosition!=-1) {
            currentList[curLoadingPosition].byteCount = count
            notifyItemChanged(curLoadingPosition)
        }
    }

    override fun onFinish() {
        if(curLoadingPosition!=-1) {
            currentList.forEach {
                it.state = StateItem.DEFAULT
                it.byteCount = 0
            }
            notifyItemRangeChanged(0, currentList.size)
        }
        curLoadingPosition = -1
    }

    open class FileHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val nameView = view.findViewById<TextView>(R.id.name_view)
        private val sizeView = view.findViewById<TextView>(R.id.size_view)

        open fun setData(itm: DeviceFileAdapterItem) {
            nameView.text = itm.deviceFile.file
            sizeView.text = itm.deviceFile.size.toString()
            val typeFace = if(itm.session==null) Typeface.BOLD else Typeface.NORMAL
            nameView.setTypeface(null, typeFace)
            sizeView.setTypeface(null, typeFace)
        }
    }

    class FileLoadingHolder(private val view: View) : FileHolder(view) {
        private val loadingCountView = view.findViewById<TextView>(R.id.loading_count_view)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

        override fun setData(itm: DeviceFileAdapterItem) {
            super.setData(itm)
            progressBar.max = itm.deviceFile.size
            progressBar.progress = itm.byteCount
            loadingCountView.text = "${itm.byteCount / 1024} / ${itm.deviceFile.size / 1024} Kb"
        }
    }
}

class FitFileListDiffUtil(): DiffUtil.ItemCallback<DeviceFileAdapterItem>() {
    override fun areItemsTheSame(oldItem: DeviceFileAdapterItem, newItem: DeviceFileAdapterItem): Boolean {
        return oldItem.deviceFile.file == newItem.deviceFile.file
                && oldItem.deviceFile.size == newItem.deviceFile.size
                && oldItem.session == newItem.session//?????????????????
                && oldItem.state == newItem.state
    }

    override fun areContentsTheSame(oldItem: DeviceFileAdapterItem, newItem: DeviceFileAdapterItem): Boolean {
        return oldItem == newItem
    }
}