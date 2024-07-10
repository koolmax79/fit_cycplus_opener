package ru.koolmax.fitopener.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FitSessionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(session: FitSessionItem)

    @Query("delete from session where file_name like :fileName")
    suspend fun delete(fileName: String)

    @Query("select * from session order by start_time desc")
    suspend fun all(): List<FitSessionItem>

    @Query("select * from session where start_time between :start and :end order by start_time")
    suspend fun allByInterval(start: Int, end: Int): List<FitSessionItem>

    @Query("select * from session where file_name like :fileName")
    suspend fun getSession(fileName: String): FitSessionItem

    @Query("select min(start_time) from session limit 1")
    suspend fun minStartTime(): Int?

    @Query("select max(start_time) from session limit 1")
    suspend fun maxStartTime(): Int?

    @Query("select count(*) count, avg(avg_heart_rate) avg_heart_rate, avg(avg_speed) avg_speed, max(max_heart_rate) max_heart_rate, sum(total_ascent) total_ascent, sum(total_descent) total_descent, sum(total_distance) total_distance, sum(total_elapsed_time) total_elapsed_time, sum(total_moving_time) total_moving_time from session where start_time between :start and :end limit 1")
    suspend fun getStatistic(start: Int, end: Int): FitStatisticItem

    @Update
    suspend fun update(session: FitSessionItem)
    //@Query("select * from session")
    //suspend fun observeAll(): Flow<List<FitSession>>
}


