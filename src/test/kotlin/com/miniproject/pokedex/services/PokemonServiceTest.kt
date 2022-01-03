package com.miniproject.pokedex.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.miniproject.pokedex.config.exception.DataNotFoundException
import com.miniproject.pokedex.config.extension.getValue
import com.miniproject.pokedex.config.extension.hitApiGet
import com.miniproject.pokedex.config.extension.storeAsValue
import com.miniproject.pokedex.config.extension.toJson
import com.miniproject.pokedex.config.property.GlobalConstants
import com.miniproject.pokedex.config.redis.RedisKey
import com.miniproject.pokedex.model.entity.PokemonEntity
import com.miniproject.pokedex.model.payload.PokemonDetailResponse
import com.miniproject.pokedex.model.payload.pokeapi.*
import com.miniproject.pokedex.model.redis.TypesRedis
import com.miniproject.pokedex.repository.PokemonRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.lang.reflect.Method
import java.net.http.HttpClient

@ExtendWith(MockitoExtension::class)
class PokemonServiceTest {
    @Mock
    private lateinit var valueOperations: ValueOperations<String, String>

    @Mock
    private lateinit var redisTemplate: StringRedisTemplate

    @Mock
    private lateinit var objectMapper: ObjectMapper

    @Mock
    private lateinit var pokemonRepository: PokemonRepository

    @InjectMocks
    private lateinit var pokemonServiceImpl: PokemonServiceImpl

    private lateinit var getPokemonTypeReflection: Method
    private lateinit var savePokemonReflection: Method

    @Test
    fun findPokemonByName_success() {
        Mockito.`when`(pokemonRepository.findByNameOrId(any(), any())).thenReturn(PokemonEntity(id = 1))

        val result = pokemonServiceImpl.findPokemonByName("1")

        Assertions.assertEquals(1L, result.id)
    }

    @Test
    fun findAllPokemon_success() {
        Mockito.`when`(objectMapper.readValue(any<String>(), any<TypeReference<PokemonApiResponse>>()))
            .thenReturn(PokemonApiResponse(_results = arrayListOf(PokemonData(name = "1"))))

        val result = pokemonServiceImpl.findAllPokemon(1, 1)

        Assertions.assertEquals("1", result.first().name)
    }

    @Test
    fun savePokemon_success() {
        savePokemonReflection = PokemonServiceImpl::class.java.getDeclaredMethod(
            "savePokemon", String::class.java
        ).apply {
            isAccessible = true
        }
        Mockito.`when`(objectMapper.readValue(any<String>(), any<TypeReference<PokemonDetailData>>()))
            .thenReturn(PokemonDetailData(name = "Pokenames"))
        Mockito.`when`(pokemonRepository.saveAndFlush(any<PokemonEntity>()))
            .thenReturn(PokemonEntity(id = 1, name = "Pokenames"))

        val result = savePokemonReflection.invoke(pokemonServiceImpl, "1") as PokemonDetailResponse

        Assertions.assertEquals("Pokenames", result.name)
    }

    @Test
    fun getPokemonType_success(){
        getPokemonTypeReflection = PokemonServiceImpl::class.java.getDeclaredMethod(
            "getPokemonType", String::class.java
        ).apply {
            isAccessible = true
        }
        val response = TypesRedis(name = "Pokenames")
        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`(valueOperations.get("${RedisKey.TYPE_KEY}_${GlobalConstants.SLUG_POKEMON_TYPE}_1"))
            .thenReturn(response.toJson())

        Mockito.`when`(objectMapper.readValue(any<String>(), any<TypeReference<TypesRedis>>()))
            .thenReturn(response)

        val result = getPokemonTypeReflection.invoke(pokemonServiceImpl, "1") as TypesRedis

        Assertions.assertEquals("Pokenames", result.name)
    }
}