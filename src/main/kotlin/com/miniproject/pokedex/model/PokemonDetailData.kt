package com.miniproject.pokedex.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("base_experience", "forms",
    "game_indices", "held_items", "is_default", "location_area_encounters", "moves",
    "order", "past_types", "species", "sprites", "stats", "types")
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
)

@JsonIgnoreProperties("slot")
data class Abilities(
    @JsonProperty(value="ability")
    var ability: Ability? = null,

    @JsonProperty(value="is_hidden")
    var _is_hidden: Boolean? = null
)

@JsonIgnoreProperties("url")
data class Ability(
    @JsonProperty(value="name")
    var name: String? = null
)