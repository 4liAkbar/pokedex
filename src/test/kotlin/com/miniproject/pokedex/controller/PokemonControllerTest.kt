package com.miniproject.pokedex.controller

import com.miniproject.pokedex.model.payload.PokemonDetailResponse
import com.miniproject.pokedex.model.payload.PokemonResponse
import com.miniproject.pokedex.services.PokemonService
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PokemonControllerTest {

    @Mock
    private lateinit var pokemonService: PokemonService

    @InjectMocks
    private lateinit var pokemonController: PokemonController

    @Test
    fun getPokemonList() {
        val response = PokemonResponse(id = 1)
        Mockito.`when`(pokemonService.findAllPokemon(any(), any()))
            .thenReturn(listOf(response))

        val result = pokemonController.getPokemonList(1, 20)
        Assertions.assertNotNull(result)
    }

    @Test
    fun getPokemonByName() {
        val response = PokemonDetailResponse(id = 1)
        Mockito.`when`(pokemonService.findPokemonByName(any()))
            .thenReturn(response)
        val result = pokemonController.getPokemonByName("1")
        Assertions.assertNotNull(result)
    }
}