package com.miniproject.pokedex.model.payload.pokeapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PokemonApiResponse(
    @JsonProperty(value = "results")
    var _results: ArrayList<PokemonData>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PokemonData(
    @JsonProperty(value = "name")
    var name: String? = null
)
