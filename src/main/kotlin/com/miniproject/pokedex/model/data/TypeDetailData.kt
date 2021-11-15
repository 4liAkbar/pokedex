package com.miniproject.pokedex.model.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("id","past_damage_relations", "game_indices", "generation", "move_damage_class", "names", "pokemon", "moves")
data class TypeDetailData (
    @JsonProperty(value="name")
    var name: String? = null,
    @JsonProperty(value="damage_relations")
    var damageRelations: DamageRelations? = null,
)

data class DamageRelations(
    @JsonProperty(value="no_damage_to")
    var noDamageTo: List<PokemonData>? = null,
    @JsonProperty(value="half_damage_to")
    var halfDamageTo: List<PokemonData>? = null,
    @JsonProperty(value="double_damage_to")
    var doubleDamageTo: List<PokemonData>? = null,
    @JsonProperty(value="no_damage_from")
    var noDamageFrom: List<PokemonData>? = null,
    @JsonProperty(value="half_damage_from")
    var halfDamageFrom: List<PokemonData>? = null,
    @JsonProperty(value="double_damage_from")
    var doubleDamageFrom: List<PokemonData>? = null
)
