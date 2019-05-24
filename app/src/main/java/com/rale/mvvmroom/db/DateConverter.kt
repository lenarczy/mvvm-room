package com.rale.mvvmroom.db

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long): Date = Date(timestamp)

    @TypeConverter
    fun toTimestamp(date: Date): Long = date.time
}
