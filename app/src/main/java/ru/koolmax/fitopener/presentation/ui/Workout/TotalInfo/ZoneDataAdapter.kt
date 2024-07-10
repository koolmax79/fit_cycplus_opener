package ru.koolmax.fitopener.presentation.ui.Workout.TotalInfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.HeartZoneInfo
import ru.koolmax.fitopener.presentation.MeasureUtil
import com.garmin.fit.Bool

class ZoneDataAdapter(context: Context, objects: List<Pair<HeartZoneInfo, Int>>) :
    ArrayAdapter<Pair<HeartZoneInfo, Int>>(context, 0, objects) {

    private val max = objects.maxOf { it.second }
    private val sum = objects.sumOf { it.second }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_zone, parent, false)
            view.tag = ZoneHolder(view)
        }
        getItem(position)?.let {
            (view!!.tag as ZoneHolder).setData(it.first, it.second, max, sum)
        }
        return view!!
    }

    class ZoneHolder(val view: View) {
        var panel: ConstraintLayout = view.findViewById(R.id.zoneLayout)
        var percentView: TextView = view.findViewById(R.id.percentView)
        var progressBar: TextView = view.findViewById(R.id.lineTextView)
        var timeView: TextView = view.findViewById(R.id.timeView)
        var rangeView: TextView = view.findViewById(R.id.rangeView)

        fun setData(zoneInfo: HeartZoneInfo, duration: Int, max: Int, sum: Int) {
            timeView.text = MeasureUtil.getDuration(duration)
            (progressBar.layoutParams as ConstraintLayout.LayoutParams).matchConstraintPercentWidth = if( sum>0 ) (duration.toFloat() / sum.toFloat()) else 0f
            percentView.text = MeasureUtil.getPercent(if( sum>0 ) (duration.toDouble() / sum * 100) else null as Double?).toList().joinToString(" ")
            progressBar.setBackgroundColor(view.resources.getColor(zoneInfo.color, null))
            //panel.setBackgroundColor(view.resources.getColor(zoneInfo.color, null))
            rangeView.text = getTextHeartRate(zoneInfo.min, zoneInfo.max)
        }

        private fun getTextHeartRate(min: Int, max: Int): String {
            if(min!=0 && max!=0) return "$min - $max"
            if(min==0 && max!=0) return "< $max"
            if(min!=0 && max==0) return "> $min"
            return ""
        }

        fun setSelected(selected: Bool) {

        }
    }
}