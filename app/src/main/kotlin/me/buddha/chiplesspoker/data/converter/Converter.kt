package me.buddha.chiplesspoker.data.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.buddha.chiplesspoker.data.model.StreetType
import java.util.Date

class Converter {

    @TypeConverter
    fun fromDate(date: Date?): String {
        return Gson().toJson(date)
    }

    @TypeConverter
    fun toDate(data: String): Date {
        val type = object : TypeToken<Date>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverters
    fun fromStreetType(type: StreetType): String {
        return type.name
    }

    @TypeConverter
    fun toStreetType(data: String): StreetType {
        return StreetType.valueOf(data)
    }
}