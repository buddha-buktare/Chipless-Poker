package me.buddha.chiplesspoker.data.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.buddha.chiplesspoker.data.model.BlindStructureEntity
import me.buddha.chiplesspoker.data.model.PlayerEntity
import me.buddha.chiplesspoker.domain.StreetType
import java.util.Date

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromDate(date: Date?): String {
        return gson.toJson(date)
    }

    @TypeConverter
    fun toDate(data: String): Date {
        val type = object : TypeToken<Date>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverters
    fun fromStreetType(type: StreetType): String {
        return type.name
    }

    @TypeConverter
    fun toStreetType(data: String): StreetType {
        return StreetType.valueOf(data)
    }

    @TypeConverter
    fun fromBlindStructure(blindStructure: BlindStructureEntity): String {
        return gson.toJson(blindStructure)
    }

    @TypeConverter
    fun toBlindStructure(blindStructureString: String): BlindStructureEntity {
        val type = object : TypeToken<BlindStructureEntity>() {}.type
        return gson.fromJson(blindStructureString, type)
    }

    @TypeConverter
    fun fromPlayerList(players: List<PlayerEntity>): String {
        return gson.toJson(players)
    }

    @TypeConverter
    fun toPlayerList(playersString: String): List<PlayerEntity> {
        val listType = object : TypeToken<List<PlayerEntity>>() {}.type
        return gson.fromJson(playersString, listType)
    }
}