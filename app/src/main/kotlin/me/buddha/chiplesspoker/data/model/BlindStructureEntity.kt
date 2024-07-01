package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.BlindStructure
import me.buddha.chiplesspoker.domain.usecase.DurationUnit

data class BlindStructureEntity(
    val durationUnit: DurationUnit,
    val blindLevels: List<BlindLevelEntity>
)

fun BlindStructureEntity.asExternalModel() = BlindStructure(
    durationUnit = this.durationUnit,
    blindLevels = this.blindLevels.map { it.asExternalModel() }
)
