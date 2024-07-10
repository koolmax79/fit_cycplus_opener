package ru.koolmax.fitopener.presentation.ui.Workout.TotalInfo

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.db.FitSessionItem
import ru.koolmax.fitopener.data.HeartZoneInfo
import ru.koolmax.fitopener.data.ble.DeviceRepository
import ru.koolmax.fitopener.data.db.FitDatabase
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.data.media.FileRepository
import ru.koolmax.fitopener.presentation.MeasureUtil
import ru.koolmax.fitopener.presentation.setText
import ru.koolmax.fitopener.presentation.ui.Main.FitViewModel
import ru.koolmax.fitopener.presentation.ui.Main.FitViewModelFactory

class TotalInfoFragment : Fragment(R.layout.fragment_total_info) {
    private val viewModel by lazy { ViewModelProvider(this, FitViewModelFactory(requireContext(),
        FileRepository(),
        FitRepository(FitDatabase.invoke(requireContext())),
        DeviceRepository()
    )
    )[FitViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fit = arguments?.getString("fit")!!

        viewModel.getFitSession(fit).observe(viewLifecycleOwner) {
            setSessionInfo(view, it)
        }

        val listView = view.findViewById<ListView>(R.id.zoneList)
        viewModel.heartZone.observe(viewLifecycleOwner) {
            listView.adapter = ZoneDataAdapter(requireContext(), it)
        }

        listView.setOnItemClickListener { parent, view2, position, id ->
            val info = listView.getItemAtPosition(position) as Pair<HeartZoneInfo, Int>
            setText(R.id.zoneInfo, info.first.label)
            setText(R.id.zoneDescription, info.first.description)
        }
    }

    private fun setSessionInfo(view: View, fitSession: FitSessionItem) {
        with(fitSession) {
            setText(R.id.startDT, MeasureUtil.getDateTime(this.startTime))
            setText(R.id.totalElapsedTime, MeasureUtil.getDuration(this.totalElapsedTime))
            setText(R.id.totalMovingTime, MeasureUtil.getDuration(this.totalMovingTime))
            setText(R.id.totalDistance, R.id.totalDistanceMeasurement, MeasureUtil.getDistance(this.totalDistance))
            setText(R.id.avgSpeed, R.id.avgSpeedMeasurement, MeasureUtil.getSpeed(this.avgSpeed))
            setText(R.id.maxSpeed, R.id.maxSpeedMeasurement, MeasureUtil.getSpeed(this.maxSpeed))
            setText(R.id.avgHeartRate, R.id.avgHeartRateMeasurement, MeasureUtil.getHeartRate(this.avgHeartRate))
            setText(R.id.minHeartRate, R.id.minHeartRateMeasurement, MeasureUtil.getHeartRate(this.minHeartRate))
            setText(R.id.maxHeartRate, R.id.maxHeartRateMeasurement, MeasureUtil.getHeartRate(this.maxHeartRate))
            setText(R.id.totalAscent, R.id.totalAscentMeasurement, MeasureUtil.getDistance(this.totalAscent))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(fit: String) =
            TotalInfoFragment().apply {
                arguments = Bundle().apply {
                    putString("fit", fit)
                }
            }
    }
}