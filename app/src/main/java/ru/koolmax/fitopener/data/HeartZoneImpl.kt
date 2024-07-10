package ru.koolmax.fitopener.data

import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.domain.IHeartZone
import java.time.LocalDate
import java.time.Period

object HeartZoneImpl: IHeartZone {
    private val info = listOf(
        HeartZoneInfo(
            "50% - 60%\nзона легкой активности",
            "низкая нагрузка развивает аэробную базу и помогает восстановиться",
            R.color.zone1,
            0,
            0
        ),
        HeartZoneInfo(
            "60% - 70%\nначало жиросжигающей зоны",
            "средняя нагрузка повышает выносливость и оптимально сжигает калории",
            R.color.zone2,
            0,
            0
        ),
        HeartZoneInfo(
            "70% - 80%\nаэробная зона",
            "высокая нагрузка способствует повышению кардиовыносливости",
            R.color.zone3,
            0,
            0
        ),
        HeartZoneInfo(
            "80% - 90%\nанаэробная зона",
            "улучшает физическую выносливость",
            R.color.zone4,
            0,
            0
        ),
        HeartZoneInfo(
            "90% - 100%\nзона VO2",
            "максимальная нагрузка помогает повысить отдачу энергии и скорость",
            R.color.zone5,
            0,
            0
        )
    )
    //private val zoneValue = mutableListOf<Short>(123, 134, 144, 155)

    override fun GetZoneInfo(): List<HeartZoneInfo> {
       return info
    }

    override fun GetZone(rate: Short): Int {
        for ((idx, v) in info.subList(1, info.size).withIndex()) {
            if(rate < v.min) return idx
        }
        return info.size-1
    }

    override fun SetBirthDay(date: LocalDate) {
        val max = 205.8f - (0.685f * GetAge(date)).toInt()
        var percent = 0.9f
        for (i in info.reversed()) {
            i.min = (max * percent).toInt()
            percent -= 0.1f
        }
        for ((idx, i) in info.withIndex()) {
            if(idx < info.size - 1)
                i.max = info[idx+1].min-1
        }
    }

    private fun GetAge(date: LocalDate) = Period.between(date, LocalDate.now()).years.toFloat()
}