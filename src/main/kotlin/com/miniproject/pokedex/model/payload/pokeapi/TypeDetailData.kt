package com.miniproject.pokedex.model.payload.pokeapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TypeDetailData(
    @JsonProperty(value = "name")
    var name: String? = null,

    @JsonProperty(value = "damage_relations")
    var damageRelations: DamageRelations? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DamageRelations(
    @JsonProperty(value = "no_damage_to")
    var noDamageTo: List<PokemonData>? = null,

    @JsonProperty(value = "half_damage_to")
    var halfDamageTo: List<PokemonData>? = null,

    @JsonProperty(value = "double_damage_to")
    var doubleDamageTo: List<PokemonData>? = null,

    @JsonProperty(value = "no_damage_from")
    var noDamageFrom: List<PokemonData>? = null,

    @JsonProperty(value = "half_damage_from")
    var halfDamageFrom: List<PokemonData>? = null,

    @JsonProperty(value = "double_damage_from")
    var doubleDamageFrom: List<PokemonData>? = null
)
