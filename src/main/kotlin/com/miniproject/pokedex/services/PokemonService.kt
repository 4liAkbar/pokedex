package com.miniproject.pokedex.services

import com.miniproject.pokedex.model.payload.PokemonDetailResponse
import com.miniproject.pokedex.model.payload.PokemonResponse

interface PokemonService {

    fun findAllPokemon(page: Int, limit: Int): List<PokemonResponse>

    fun findPokemonByName(name: String): PokemonDetailResponse
}