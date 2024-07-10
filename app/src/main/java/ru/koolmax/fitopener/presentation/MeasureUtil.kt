package ru.koolmax.fitopener.presentation

import android.widget.TextView
import androidx.fragment.app.Fragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MeasureUtil {
    companion object {
        const val DistanceUnit = "км"
        const val AscentUnit = "м"
        const val SpeedUnit = "км/ч"
        const val HeartRateUnit = "уд/м"

        private val empty = Pair("-", "")
        private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        fun getDateTime(v: LocalDateTime?): String {
            return if (v != null) v.format(dateTimeFormat) else "-"
        }

        fun getDuration(v: Int?): String {
            return if (v != null) String.format("%d:%02d:%02d", v / 3600, (v / 60) % 60, (v % 60).toInt()) else "-"
        }

        fun getDistance(v: Int?): Pair<String, String> {
            return if (v != null)
                if (v < 1000) Pair("$v", AscentUnit) else Pair(String.format("%.2f", v.toFloat() / 1000f), DistanceUnit)
            else empty
        }

        fun getDistanceForChart(v: Int?): Pair<String, String> {
            return if (v != null)
                if (v < 1000) Pair("$v", AscentUnit) else Pair(String.format("%.0f", v.toFloat() / 1000f), DistanceUnit)
            else empty
        }

        fun getAscent(v: UInt?): Pair<String, String> {
            return if (v != null)
                Pair("$v", AscentUnit)
            else empty
        }

        fun getSpeed(v: Double?): Pair<String, String> {
            return if (v != null) Pair(String.format("%.1f", v), SpeedUnit) else empty
        }

        fun getHeartRate(v: Int?): Pair<String, String> {
            return if (v != null) Pair("$v", HeartRateUnit) else empty
        }

        fun getGrade(v: Double?): Pair<String, String> {
            return if (v != null) Pair(String.format("%.1f", v), "%") else empty
        }

        fun getPercent(v: Double?): Pair<String, String> {
            return if (v != null) Pair(String.format("%.1f", v), "%") else empty
        }
    }
}

fun Fragment.setText(valId: Int, str: String) {
    (view?.findViewById(valId) as TextView).text = str ?: "-"
}

fun Fragment.setText(valId: Int, measurementId: Int, str: Pair<String, String>) {
    (view?.findViewById(valId) as TextView).text = str.first ?: "-"
    (view?.findViewById(measurementId) as TextView).text = str.second ?: ""
}
