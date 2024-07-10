package ru.koolmax.fitopener.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 2,
    entities = [FitSessionItem::class],
    exportSchema = false)
abstract class FitDatabase: RoomDatabase() {
    abstract fun sessions(): FitSessionDAO

    companion object {
        @Volatile
        private var instance: FitDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: create(context).also { instance = it }
        }

        private fun create(context: Context) =
            Room.databaseBuilder(context.applicationContext, FitDatabase::class.java, "fit.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}