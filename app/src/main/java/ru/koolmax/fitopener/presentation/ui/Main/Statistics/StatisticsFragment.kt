package ru.koolmax.fitopener.presentation.ui.Main.Statistics

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.db.FitDatabase
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.presentation.MeasureUtil
import ru.koolmax.fitopener.presentation.setText
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.shawnlin.numberpicker.NumberPicker

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel by lazy { ViewModelProvider(this, StatisticsViewModelFactory(requireContext(), FitRepository(
        FitDatabase.invoke(this.requireContext()))
    )
    )[StatisticsViewModel::class.java] }
    private val groupChip by lazy { view?.findViewById<ChipGroup>(R.id.chip_group)!! }

    private fun getCurrentChartType() = when(groupChip.checkedChipId) {
            R.id.chip_distance -> ChartType.DISTANCE
            R.id.chip_ascent -> ChartType.ASCENT
            R.id.chip_moving_time -> ChartType.MOVING_TIME
            R.id.chip_heart_rate -> ChartType.HEART_RATE
            R.id.chip_speed -> ChartType.SPEED
            else -> ChartType.NONE
        }

    private fun getBarColor(chart: ChartType) = when(chart) {
        ChartType.DISTANCE -> R.color.statistics_distance
        ChartType.ASCENT -> R.color.statistics_chip_ascent
        ChartType.MOVING_TIME -> R.color.statistics_chip_moving_time
        ChartType.HEART_RATE -> R.color.statistics_chip_heart_rate
        ChartType.SPEED -> R.color.statistics_chip_speed
        else -> R.color.black
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val numberPicker = view.findViewById<NumberPicker>(R.id.edit_year)
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            viewModel.getStatistic(newVal)
            viewModel.getChartData(getCurrentChartType())
        }

        viewModel.RangeYear.observe(viewLifecycleOwner) {
            numberPicker.minValue = viewModel.RangeYear.value!!.first
            numberPicker.maxValue = viewModel.RangeYear.value!!.second
            numberPicker.value = numberPicker.maxValue
            viewModel.getStatistic(numberPicker.value)
        }
        viewModel.getRangeYear()

        val chart = view.findViewById<BarChart>(R.id.chart)
        chart.description.text = ""

        chart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val bundle = Bundle()
                bundle.putString("fit", e?.data.toString() )
                Navigation.findNavController(view).navigate(R.id.navigation_workout, bundle)
            }

            override fun onNothingSelected() {
            }
        })

        var labelX = ""
        var barFormatter: ValueFormatter = DefaultValueFormatter(2)

        groupChip.setOnCheckedStateChangeListener { chipGroup, ints ->
            if(ints.size==1) {
                val chipId = ints.first()
                val chip = view.findViewById<Chip>(chipId)
                when (getCurrentChartType()) {
                    ChartType.DISTANCE -> {
                        barFormatter = DistanceValueFormatter()
                        labelX = chip.text.toString() + ", " + MeasureUtil.DistanceUnit
                        viewModel.getChartData(ChartType.DISTANCE)
                    }

                    ChartType.ASCENT -> {
                        barFormatter = DefaultValueFormatter(0)
                        labelX = chip.text.toString() + ", " + MeasureUtil.AscentUnit
                        viewModel.getChartData(ChartType.ASCENT)
                    }

                    ChartType.MOVING_TIME -> {
                        barFormatter = DurationValueFormatter()
                        labelX = chip.text.toString()
                        viewModel.getChartData(ChartType.MOVING_TIME)
                    }

                    ChartType.HEART_RATE -> {
                        barFormatter = DefaultValueFormatter(0)
                        labelX = chip.text.toString() + ", " + MeasureUtil.HeartRateUnit
                        viewModel.getChartData(ChartType.HEART_RATE)
                    }

                    ChartType.SPEED -> {
                        barFormatter = DefaultValueFormatter(1)
                        labelX = chip.text.toString() + ", " + MeasureUtil.SpeedUnit
                        viewModel.getChartData(ChartType.SPEED)
                    }
                    ChartType.NONE -> { }
                }
            }
        }
        //distanceChip.isCheckedIconVisible = true

        chart.apply {
            //axisLeft.mEntries = ArrayList<Int>()
            xAxis.setDrawGridLinesBehindData(true)
            //xAxis.typeface = tfRegular
            xAxis.setGranularity(1f)
            xAxis.setCenterAxisLabels(true)
            val typedValue = TypedValue()
            view.context.theme.resolveAttribute(android.R.attr.textColorSecondary, typedValue, true)
            xAxis.textColor = ContextCompat.getColor(view.context, typedValue.resourceId)
            axisLeft.textColor = xAxis.textColor
            axisRight.textColor = xAxis.textColor
            legend.textColor = xAxis.textColor
        }


        //chart.xAxis.axisMinimum = 0f
        //chart.xAxis.axisMaximum = 10f

        viewModel.FitSessionList.observe(viewLifecycleOwner) {
            viewModel.getChartData(getCurrentChartType())
        }

        viewModel.EntryList.observe(viewLifecycleOwner) {
            //val xLabel = ArrayList<String>()
            //xLabel.add("fdsf")
            //chart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)

            val dataSet = BarDataSet(it, labelX)

            dataSet.color = view.resources.getColor(getBarColor(getCurrentChartType()), view.context.theme)
            dataSet.setDrawValues(false)

            chart.axisLeft.valueFormatter = barFormatter
            chart.axisRight.valueFormatter = barFormatter

            val barData = BarData(dataSet)
            //barData.setValueFormatter(barFormatter)
            //barData.barWidth = 0.2f
            chart.data = barData
            chart.invalidate()
        }

        viewModel.SessionStatistic.observe(viewLifecycleOwner) {
            setText(R.id.view_training_count, it.count.toString())
            setText(R.id.view_total_distance, R.id.view_total_distance_measurement, MeasureUtil.getDistance(it.totalDistance))
            setText(R.id.view_total_ascent, R.id.view_total_ascent_measurement, MeasureUtil.getDistance(it.totalAscent))
            setText(R.id.view_total_elapsed_time, MeasureUtil.getDuration(it.totalElapsedTime))
            setText(R.id.view_total_moving_time, MeasureUtil.getDuration(it.totalMovingTime))
        }
    }
}