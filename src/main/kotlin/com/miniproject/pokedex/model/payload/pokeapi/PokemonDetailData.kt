package com.miniproject.pokedex.model.payload.pokeapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PokemonDetailData(
    @JsonProperty(value = "id")
    var id: Long? = 0,
    @JsonProperty(value = "name")
    var name: String? = null,
    @JsonProperty(value = "height")
    var _height: Int? = 0,
    @JsonProperty(value = "weight")
    var _weight: Int? = 0,
    @JsonProperty(value = "abilities")
    var abilities: List<Abilities>? = null,
    @JsonProperty(value = "types")
    var types: List<Types>? = null,
    @JsonProperty(value = "sprites")
    var sprites: Sprites? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Abilities(
    @JsonProperty(value = "ability")
    var ability: PokemonData? = null,

    @JsonProperty(value = "is_hidden")
    var isHidden: Boolean? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Types(
    @JsonProperty(value = "type")
    var type: PokemonData? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Sprites(
    @JsonProperty(value = "back_default")
    var backDefault: String? = null,
    @JsonProperty(value = "front_default")
    var frontDefault: String? = null
)