package me.buddha.chiplesspoker.data.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.buddha.chiplesspoker.data.model.BlindStructureEntity
import me.buddha.chiplesspoker.data.model.HandEntity
import me.buddha.chiplesspoker.data.model.PlayerEntity
import me.buddha.chiplesspoker.data.model.PotEntity
import me.buddha.chiplesspoker.domain.utils.DurationUnit
import me.buddha.chiplesspoker.domain.utils.PlayerMove
import me.buddha.chiplesspoker.domain.utils.PlayingStatus
import me.buddha.chiplesspoker.domain.utils.StreetType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

    private val gson = Gson()
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromDate(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toDate(data: String?): LocalDateTime? {
        return data?.let { LocalDateTime.parse(it, formatter) } ?: LocalDateTime.now()
    }

    @TypeConverters
    fun fromStreetType(type: StreetType): String {
        return type.name
    }

    @TypeConverter
    fun toStreetType(data: String): StreetType {
        return StreetType.valueOf(data)
    }

    @TypeConverters
    fun fromDurationUnit(type: DurationUnit): String {
        return type.name
    }

    @TypeConverter
    fun toDurationUnit(data: String): DurationUnit {
        return DurationUnit.valueOf(data)
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

    @TypeConverter
    fun fromPotList(players: List<PotEntity>): String {
        return gson.toJson(players)
    }

    @TypeConverter
    fun toPotList(playersString: String): List<PotEntity> {
        val listType = object : TypeToken<List<PotEntity>>() {}.type
        return gson.fromJson(playersString, listType)
    }

    @TypeConverter
    fun fromHand(hand: HandEntity): String {
        return gson.toJson(hand)
    }

    @TypeConverter
    fun toHand(data: String): HandEntity {
        val type = object : TypeToken<HandEntity>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverters
    fun fromPlayingStatus(status: PlayingStatus): String {
        return status.name
    }

    @TypeConverter
    fun toPlayingStatus(data: String): PlayingStatus {
        return PlayingStatus.valueOf(data)
    }

    @TypeConverters
    fun fromPlayerMove(move: PlayerMove): String {
        return move.name
    }

    @TypeConverter
    fun toPlayerMove(data: String): PlayerMove {
        return PlayerMove.valueOf(data)
    }
}