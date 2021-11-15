package com.miniproject.pokedex.model.payload.pokeapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.miniproject.pokedex.model.data.PokemonData

@JsonIgnoreProperties("count","next","previous")
data class PokemonApiResponse (
    @JsonProperty(value="results")
    var _results: ArrayList<PokemonData>? = null
)
