package ru.koolmax.fitopener.domain

import ru.koolmax.fitopener.data.HeartZoneInfo
import java.time.LocalDate

interface IHeartZone {
    fun GetZoneInfo(): List<HeartZoneInfo>
    fun GetZone(rate: Short): Int
    fun SetBirthDay(date: LocalDate)
}