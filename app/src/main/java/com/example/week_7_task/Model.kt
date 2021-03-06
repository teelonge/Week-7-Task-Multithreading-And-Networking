package com.example.week_7_task


data class Pokemons(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Result>
)


data class Result(
    val name: String,
    val url: String
)

data class PokemonDetails(
    val id: Int,
    val abilities: List<Ability>,
    val base_experience: Int,
    val height: Int,
    val held_items: List<Any>,
    val is_default: Boolean,
    val location_area_encounters: String,
    val moves: List<Move>,
    val name: String,
    val order: Int,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int
)

data class Ability(
    val ability: AbilityX,
    val is_hidden: Boolean,
    val slot: Int
)


data class Move(
    val move: MoveX
)

data class Sprites(
    val back_default: String?,
    val back_female: Any?,
    val back_shiny: String?,
    val back_shiny_female: Any?,
    val front_default: String?,
    val front_female: Any?,
    val front_shiny: String?,
    val front_shiny_female: Any?,
)

data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: StatX
)

data class Type(
    val slot: Int,
    val type: TypeX
)

data class AbilityX(
    val name: String,
    val url: String
)


data class MoveX(
    val name: String,
    val url: String
)

data class StatX(
    val name: String,
    val url: String
)

data class TypeX(
    val name: String,
    val url: String
)

data class UploadResponse(
    val status: Int,
    val message: String,
    val payload: Payload,
)

data class Payload(
    val fileId: String,
    val fileType: String,
    val fileName: String,
    val downloadUri: String,
    val uploadStatus: Boolean
)
