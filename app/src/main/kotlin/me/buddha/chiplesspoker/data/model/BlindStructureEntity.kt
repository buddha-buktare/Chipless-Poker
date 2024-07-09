package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.BlindStructure
import me.buddha.chiplesspoker.domain.utils.DurationUnit

data class BlindStructureEntity(
    val durationUnit: DurationUnit,
    val blindLevels: List<BlindLevelEntity>,
    val remainingHands: Long = 0,
    val remainingTime: Long = 0,
    val currentLevel: Int = 0,
)

fun BlindStructureEntity.asExternalModel() = BlindStructure(
    durationUnit = this.durationUnit,
    blindLevels = this.blindLevels.map { it.asExternalModel() },
    remainingHands = this.remainingHands,
    remainingTime = this.remainingTime,
    currentLevel = this.currentLevel,
)
