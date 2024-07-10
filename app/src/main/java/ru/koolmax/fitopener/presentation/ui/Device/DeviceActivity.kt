package ru.koolmax.fitopener.presentation.ui.Device

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.DeviceFile
import ru.koolmax.fitopener.data.ble.DeviceRepository
import ru.koolmax.fitopener.data.db.FitDatabase
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.data.db.FitSessionItem
import ru.koolmax.fitopener.data.media.FileRepository
import ru.koolmax.fitopener.presentation.ui.Main.FitViewModel
import ru.koolmax.fitopener.presentation.ui.Main.FitViewModelFactory

class DeviceActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this, FitViewModelFactory(application,
        FileRepository(),
        FitRepository(FitDatabase.invoke(this)),
        DeviceRepository()
    )
    )[FitViewModel::class.java] }
    private lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_device)

        //val toolBar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolBar)
        supportActionBar?.setIcon(R.drawable.directions_bike)
        supportActionBar?.title = ""

        intent.getStringExtra("address")?.let{
            address = it
        }

        val listView = findViewById<RecyclerView>(R.id.fit_file_list)
        val adapter = FitFileDataAdapter()
        listView.adapter = adapter
        adapter.onItemClickListener = {
            adapter.startLoadFile(it.deviceFile.file)
            viewModel.SaveDeviceFileToLib(it.deviceFile.file)
        }

        viewModel.fitSessionList.observe(this) {
            viewModel.deviceFileList.value?.let { list ->
                adapter.submitList(createList(list, it))
            }
        }

        viewModel.deviceFileList.observe(this) {
            adapter.submitList(createList(it, viewModel.fitSessionList.value))
        }

        viewModel.getSessionList()

        viewModel.connect(address, adapter)
    }

    private fun createList(fitFileRecord: List<DeviceFile>, fitSessionItem: List<FitSessionItem>?): List<DeviceFileAdapterItem> {
        val list = mutableListOf<DeviceFileAdapterItem>()
        for (file in fitFileRecord.sortedBy { it.file }) {
            val session = fitSessionItem?.find { it.fileName==file.file }
            list.add(DeviceFileAdapterItem(file, session))
        }
        return list
    }
}
