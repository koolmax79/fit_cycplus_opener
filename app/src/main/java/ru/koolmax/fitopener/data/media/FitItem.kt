package ru.koolmax.fitopener.data.media

import ru.koolmax.fitopener.data.HeartZoneInfo
import ru.koolmax.fitopener.domain.IHeartZone
import com.garmin.fit.RecordMesg
import com.garmin.fit.RecordMesgListener
import com.github.mikephil.charting.data.Entry

class FitItem: RecordMesgListener {
    enum class FitListType { ALTITUDE, GRADE, SPEED, HEART, CADENCE, TEMPERATURE, HEART_TIME }
    val records = mapOf(
        FitListType.ALTITUDE to mutableListOf<Entry>(),
        FitListType.GRADE to mutableListOf<Entry>(),
        FitListType.SPEED to mutableListOf<Entry>(),
        FitListType.HEART to mutableListOf<Entry>(),
        FitListType.CADENCE to mutableListOf<Entry>(),
        FitListType.TEMPERATURE to mutableListOf<Entry>(),
        FitListType.HEART_TIME to mutableListOf<Entry>()
    )

    var heart = sortedMapOf<Short, Int>()
    private var lastDT: Long = 0

    override fun onMesg(p0: RecordMesg?) {
        p0?.let{
            if (it.speed != null) {
                records[FitListType.SPEED]!!.add(Entry(it.distance, it.speed * 3.6f))
            }
            if (it.heartRate != null && it.heartRate.toInt() != 0) {
                records[FitListType.HEART]!!.add(Entry(it.distance, it.heartRate.toFloat()))
                if (lastDT > 0) {
                    val v = heart[it.heartRate]
                if (v != null)
                    heart[it.heartRate] = v + (it.timestamp.timestamp - lastDT).toInt()
                else
                    heart[it.heartRate] = (it.timestamp.timestamp - lastDT).toInt()
                }
                lastDT = it.timestamp.timestamp
            }

            if (it.cadence != null) {
                records[FitListType.CADENCE]!!.add(Entry(it.distance, it.cadence.toFloat()))
            }
            if (it.altitude != null) {
                records[FitListType.ALTITUDE]!!.add(Entry(it.distance, it.altitude))
            }
            if (it.grade != null) {
                records[FitListType.GRADE]!!.add(Entry(it.distance, it.grade))
            }
            if (it.temperature != null) {
                records[FitListType.TEMPERATURE]!!.add(Entry(it.distance, it.temperature.toFloat()))
            }
        }
    }

    fun endLoad() {
        val list = records[FitListType.HEART_TIME]
        for(i in heart) {
            list?.add(Entry(i.key.toFloat(), i.value.toFloat()))
        }
    }

    fun getHeartZone(heartZone: IHeartZone): List<Pair<HeartZoneInfo, Int>> {
        val zone = MutableList<Int>(heartZone.GetZoneInfo().size) { 0 }

        for (i in heart) {
            val zoneIdx = heartZone.GetZone(i.key)
            zone[zoneIdx] = zone[zoneIdx] + i.value
        }
        return zone.mapIndexed { idx, value -> heartZone.GetZoneInfo()[idx] to value }
    }
}