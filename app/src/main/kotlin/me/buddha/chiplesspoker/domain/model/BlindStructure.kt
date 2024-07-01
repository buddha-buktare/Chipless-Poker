package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.BlindStructureEntity
import me.buddha.chiplesspoker.domain.usecase.DurationUnit

data class BlindStructure(
    var durationUnit: DurationUnit = DurationUnit.HANDS,
    val blindLevels: List<BlindLevel> = listOf()
)

fun BlindStructure.asEntity() = BlindStructureEntity(
    durationUnit = this.durationUnit,
    blindLevels = this.blindLevels.map { it.asEntity() }
)