package com.miniproject.pokedex.services

import com.miniproject.pokedex.model.redis.TypesRedis
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks

internal class TypesServiceTest {


    @InjectMocks
    private lateinit var typesServiceTestImpl: TypesServiceImpl

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