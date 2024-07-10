package ru.koolmax.fitopener.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters

@Entity(tableName = "session")
@TypeConverters(FitConverter::class)
data class FitStatisticItem(
    @ColumnInfo(name="count")
    var count: Int,
    @ColumnInfo(name="avg_heart_rate")
    var avgHeartRate: Int,
    @ColumnInfo(name="avg_speed")
    var avgSpeed: Double,
    @ColumnInfo(name="max_heart_rate")
    var maxHeartRate: Int,
    @ColumnInfo(name="total_ascent")
    var totalAscent: Int,
    @ColumnInfo(name="total_descent")
    var totalDescent: Int,
    @ColumnInfo(name="total_distance")
    var totalDistance: Int,
    @ColumnInfo(name="total_elapsed_time")
    var totalElapsedTime: Int,
    @ColumnInfo(name="total_moving_time")
    var totalMovingTime: Int
)