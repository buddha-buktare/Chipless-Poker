package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.BlindLevel

data class BlindLevelEntity(
    val level: Int,
    val big: Long,
    val small: Long,
    val duration: Long,
)

fun BlindLevelEntity.asExternalModel() = BlindLevel(
    level = this.level,
    big = this.big,
    small = this.small,
    duration = this.duration
)
