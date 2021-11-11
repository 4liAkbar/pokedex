package com.miniproject.pokedex.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("base_experience", "forms",
    "game_indices", "held_items", "is_default", "location_area_encounters", "moves",
    "order", "past_types", "species", "stats")
data class PokemonDetailData(
    @JsonProperty(value="id")
    var id: Int? = 0,
    @JsonProperty(value="name")
    var name: String? = null,
    @JsonProperty(value="height")
    var _height: Int? = 0,
    @JsonProperty(value="weight")
    var _weight: Int? = 0,
    @JsonProperty(value="abilities")
    var abilities: List<Abilities>? = null,
    @JsonProperty(value="types")
    var types: List<Types>? = null,
    @JsonProperty(value="sprites")
    var sprites: Sprites? = null
)

@JsonIgnoreProperties("slot")
data class Abilities(
    @JsonProperty(value="ability")
    var ability: PokemonData? = null,

    @JsonProperty(value="is_hidden")
    var isHidden: Boolean? = null
)

@JsonIgnoreProperties("slot")
data class Types(
    @JsonProperty(value="type")
    var type: PokemonData? = null
)

@JsonIgnoreProperties("back_female", "back_shiny", "back_shiny_female",
    "front_female", "front_shiny", "front_shiny_female", "other", "versions")
data class Sprites(
    @JsonProperty(value="back_default")
    var backDefault: String? = null,
    @JsonProperty(value="front_default")
    var frontDefault: String? = null
)