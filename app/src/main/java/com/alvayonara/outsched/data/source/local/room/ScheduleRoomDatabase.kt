package com.alvayonara.outsched.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity

@Database(entities = [ScheduleEntity::class], version = 1)
abstract class ScheduleRoomDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: ScheduleRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ScheduleRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ScheduleRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ScheduleRoomDatabase::class.java, "schedule_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE as ScheduleRoomDatabase
        }
    }
}