package com.miniproject.pokedex.model.payload

import com.fasterxml.jackson.annotation.JsonProperty

data class PokemonResponse (
    @JsonProperty(value="id")
    var id: Int? = null,
    @JsonProperty(value="name")
    var name: String? = null
)
