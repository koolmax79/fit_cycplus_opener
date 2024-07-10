package ru.koolmax.fitopener.presentation.ui.Main.Statistics

import ru.koolmax.fitopener.presentation.MeasureUtil
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class DurationValueFormatter: ValueFormatter() {
    override fun getBarLabel(barEntry: BarEntry?): String {
        return MeasureUtil.getDuration(barEntry?.y?.toInt())
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return MeasureUtil.getDuration(value.toInt())
    }
}

class DistanceValueFormatter: ValueFormatter() {
    override fun getBarLabel(barEntry: BarEntry?): String {
        return MeasureUtil.getDistance(barEntry?.y?.toInt()).first
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return MeasureUtil.getDistanceForChart(value.toInt()).first
    }
}