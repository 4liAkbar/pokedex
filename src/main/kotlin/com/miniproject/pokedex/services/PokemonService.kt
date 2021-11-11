package com.miniproject.pokedex.services

import com.miniproject.pokedex.model.PokemonDetailResponse
import com.miniproject.pokedex.model.PokemonResponse

interface PokemonService {
    fun findAllPokemon(start : Int?, limit : Int?) : List<PokemonResponse>
    fun findPokemonByName(name : String): PokemonDetailResponse
}