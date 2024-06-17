package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.BlindStructure

data class BlindStructureEntity(
    val durationUnit: String,
    val levels: List<BlindLevelEntity>
)

fun BlindStructureEntity.asExternalModel() = BlindStructure(
    durationUnit = this.durationUnit,
    levels = this.levels.map { it.asExternalModel() }
)
