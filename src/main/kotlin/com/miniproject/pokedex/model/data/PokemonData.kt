package com.miniproject.pokedex.model.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("url")
data class PokemonData (
    @JsonProperty(value="name")
    var name: String? = null
)