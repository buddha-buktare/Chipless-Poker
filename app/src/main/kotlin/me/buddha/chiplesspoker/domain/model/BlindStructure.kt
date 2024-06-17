package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.BlindStructureEntity

data class BlindStructure(
    val durationUnit: String,
    val levels: List<BlindLevel>
)

fun BlindStructure.asEntity() = BlindStructureEntity(
    durationUnit = this.durationUnit,
    levels = this.levels.map { it.asEntity() }
)