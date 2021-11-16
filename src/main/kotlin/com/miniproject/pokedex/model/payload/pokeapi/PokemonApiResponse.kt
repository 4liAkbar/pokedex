package com.miniproject.pokedex.model.payload.pokeapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PokemonApiResponse(
    @JsonProperty(value = "results")
    var _results: ArrayList<PokemonData>? = null
)
