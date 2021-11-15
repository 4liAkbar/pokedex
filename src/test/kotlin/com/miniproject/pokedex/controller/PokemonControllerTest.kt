package com.miniproject.pokedex.controller

import com.miniproject.pokedex.model.payload.PokemonDetailResponse
import com.miniproject.pokedex.services.PokemonService
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus


internal class PokemonControllerTest {

    @Mock
    private lateinit var pokemonService: PokemonService

    @InjectMocks
    private lateinit var controller: PokemonController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getPokemon(){
        Mockito.`when`(pokemonService.findPokemonByName("1"))
            .thenReturn(PokemonDetailResponse())

        Mockito.`when`(pokemonService.findAllPokemon(0, 20))
            .thenReturn(null)

    }
}