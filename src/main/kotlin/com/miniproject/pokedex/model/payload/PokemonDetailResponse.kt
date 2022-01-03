package com.miniproject.pokedex.model.payload

import com.fasterxml.jackson.annotation.JsonProperty

data class PokemonDetailResponse(
    @JsonProperty(value = "id")
    var id: Long? = null,

    @JsonProperty(value = "name")
    var name: String? = null,

    @JsonProperty(value = "type")
    var type: List<String>? = null,

    @JsonProperty(value = "sprite")
    var sprite: String? = null,

    @JsonProperty(value = "weakness")
    var weakness: List<String>? = null,

    @JsonProperty(value = "resistance")
    var resistance: List<String>? = null,

    @JsonProperty(value = "description")
    var description: String? = null

)
