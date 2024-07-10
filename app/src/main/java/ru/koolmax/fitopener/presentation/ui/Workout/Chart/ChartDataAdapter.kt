package ru.koolmax.fitopener.presentation.ui.Workout.Chart

import android.R.*
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.media.FitItem
import ru.koolmax.fitopener.data.FitTypeImpl
import ru.koolmax.fitopener.presentation.ui.Main.Statistics.DistanceValueFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ChartDataAdapter(context: Context, objects: List<Pair<FitItem.FitListType, List<Entry>>>):
    ArrayAdapter<Pair<FitItem.FitListType, List<Entry>>>(context, 0, objects) {

        private val textColor by lazy {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.textColorSecondary, typedValue, true)
            ContextCompat.getColor(context, typedValue.resourceId)
        }

    private val altitudeValues = objects.firstOrNull { it.first== FitItem.FitListType.ALTITUDE }?.second

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_linechart, parent, false)
            view.tag = Holder(view)
        }
        getItem(position)?.let {
            (view!!.tag as Holder).setData(it, altitudeValues, textColor)
        }
        return view!!
    }

    class Holder(private val view: View) {
        private val lineChart = view.findViewById<LineChart>(R.id.chart)

        private fun makeDataSet(type: FitItem.FitListType, values: List<Entry>): LineDataSet {
            val dataSet = LineDataSet(values, FitTypeImpl.info[type]?.caption)
            dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            dataSet.color = view.resources.getColor(FitTypeImpl.info[type]?.color!!, null)
            dataSet.setDrawCircles(false)
            dataSet.setDrawValues(false)

            dataSet.mode = LineDataSet.Mode.LINEAR
            dataSet.fillColor = dataSet.color
            //dataSet.fillAlpha = 100
            dataSet.setDrawFilled(true)
            return dataSet
        }

        fun setData(data: Pair<FitItem.FitListType, List<Entry>>, altitudeValues: List<Entry>?, textColor: Int) {
            val lineData = LineData()
            if ((data.first == FitItem.FitListType.SPEED || data.first == FitItem.FitListType.HEART || data.first == FitItem.FitListType.CADENCE)
                && altitudeValues!=null) {
                lineData.addDataSet(makeDataSet(FitItem.FitListType.ALTITUDE, altitudeValues))
            }
            lineData.addDataSet(makeDataSet(data.first, data.second))
            lineChart.data = lineData
            lineChart.setDrawGridBackground(false)
            //val description = Description()
            //description.text = FitTypeImpl.info[type]?.caption
            //description.textSize = 15f
            //lineChart.description = description
            lineChart.description.isEnabled = false

            lineChart.legend.textColor = textColor
            lineChart.xAxis.textColor = textColor
            lineChart.axisLeft.textColor = textColor
            lineChart.axisRight.textColor = textColor

            lineChart.xAxis.valueFormatter = DistanceValueFormatter()

            lineChart.setNoDataText("Нет данных")
            lineChart.setNoDataTextColor(textColor)
            lineChart.setDrawBorders(true)
            lineChart.setDrawGridBackground(false)
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        }
    }
}