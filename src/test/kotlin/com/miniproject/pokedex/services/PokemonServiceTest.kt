package com.miniproject.pokedex.services

import com.miniproject.pokedex.model.payload.PokemonDetailResponse
import com.miniproject.pokedex.model.payload.PokemonResponse
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations


internal class PokemonServiceTest {
    @Mock
    lateinit var valueOperations: ValueOperations<String, String>

    @Mock
    private lateinit var redisTemplate: StringRedisTemplate

    @InjectMocks
    private lateinit var typesServiceTestImpl: TypesServiceImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
    }

    @Test
    fun findAllPokemon(): ArrayList<PokemonResponse>? {
        return null
    }

    @Test
    fun findPokemonByName(): PokemonDetailResponse {
        return PokemonDetailResponse()
    }
}