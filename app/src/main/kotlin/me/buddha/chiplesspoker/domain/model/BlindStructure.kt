package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.BlindStructureEntity
import me.buddha.chiplesspoker.domain.utils.DurationUnit

data class BlindStructure(
    var durationUnit: DurationUnit = DurationUnit.HANDS,
    val blindLevels: List<BlindLevel> = listOf(),
    val remainingHands: Long = 0,
    val remainingTime: Long = 0,
    val currentLevel: Long = 0,
)

fun BlindStructure.asEntity() = BlindStructureEntity(
    durationUnit = this.durationUnit,
    blindLevels = this.blindLevels.map { it.asEntity() },
    remainingHands = this.remainingHands,
    remainingTime = this.remainingTime,
    currentLevel = this.currentLevel,
)