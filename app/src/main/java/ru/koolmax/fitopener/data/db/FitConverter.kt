package ru.koolmax.fitopener.data.db

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

public class FitConverter {
    @TypeConverter
    public fun toLocalDateTime(v: LocalDateTime): Int {
        return v.toEpochSecond(ZoneOffset.UTC).toInt()
    }

    @TypeConverter
    public fun fromLocalDateTime(v: Int): LocalDateTime {
        return LocalDateTime.ofEpochSecond(v.toLong(), 0, ZoneOffset.UTC)
    }
}