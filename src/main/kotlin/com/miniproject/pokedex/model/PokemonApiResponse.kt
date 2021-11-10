package com.miniproject.pokedex.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("count","next","previous")
data class PokemonApiResponse (
    @JsonProperty(value="results")
    var _results: ArrayList<PokemonData>? = null
)
