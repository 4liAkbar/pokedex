package com.miniproject.pokedex.services

import com.miniproject.pokedex.model.redis.TypesRedis
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations

internal class TypesServiceTest {
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
    fun getPokemonType(name: String): TypesRedis {
        return storeToRedisAndGetTypePokemon(name)
    }

    @Test
    fun storeToRedisAndGetTypePokemon(name: String): TypesRedis {
        return TypesRedis(
            name = name,
            weakness = null,
            resistance = null
        )
    }
}