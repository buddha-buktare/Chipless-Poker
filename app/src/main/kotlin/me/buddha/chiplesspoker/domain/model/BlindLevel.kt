package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.BlindLevelEntity

data class BlindLevel(
    val level: Int,
    val big: Long,
    val small: Long,
    val duration: Long,
)

fun BlindLevel.asEntity() = BlindLevelEntity(
    level = this.level,
    big = this.big,
    small = this.small,
    duration = this.duration
)