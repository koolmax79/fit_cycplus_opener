package ru.koolmax.fitopener.presentation.ui.Main.FitList

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.db.FitSessionItem
import ru.koolmax.fitopener.presentation.MeasureUtil

class SessionDataAdapter(): ListAdapter<FitSessionItem, SessionDataAdapter.Holder>(FitListDiffUtil()) {

    var onItemClickListener: ((FitSessionItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
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
        private val startDTView = view.findViewById<TextView>(R.id.startDT)
        private val totalElapsedTimeView = view.findViewById<TextView>(R.id.totalElapsedTime)
        private val totalMovingTimeView = view.findViewById<TextView>(R.id.totalMovingTime)
        private val totalDistanceView = view.findViewById<TextView>(R.id.totalDistance)

        fun setData(itm: FitSessionItem) {
            startDTView.setTypeface(null, if (itm.displayed==1) Typeface.NORMAL else Typeface.BOLD)
            startDTView.text = MeasureUtil.getDateTime(itm.startTime)
            totalElapsedTimeView.text = MeasureUtil.getDuration(itm.totalElapsedTime)
            totalMovingTimeView.text = MeasureUtil.getDuration(itm.totalMovingTime)
            totalDistanceView.text = MeasureUtil.getDistance(itm.totalDistance).toList().joinToString(" ")
        }
    }
}