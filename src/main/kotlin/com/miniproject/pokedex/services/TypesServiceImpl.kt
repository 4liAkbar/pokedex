package com.miniproject.pokedex.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.miniproject.pokedex.config.extension.*
import com.miniproject.pokedex.config.property.GlobalConstants
import com.miniproject.pokedex.config.property.GlobalConstants.SLUG_POKEMON_TYPE
import com.miniproject.pokedex.config.redis.RedisKey
import com.miniproject.pokedex.model.payload.pokeapi.TypeDetailData
import com.miniproject.pokedex.model.redis.TypesRedis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class TypesServiceImpl @Autowired constructor(
    private val redisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper,
) : TypesService {

    override fun getPokemonType(name: String): TypesRedis? {
        val data = when (val typesRedis =
            redisTemplate.getValue("${REDIS_KEY}_${SLUG_POKEMON_TYPE}_$name")) {
            null -> storeToRedisAndGetTypePokemon(name)
            else -> objectMapper.readValue(typesRedis)
        }

        return data
    }

    private fun storeToRedisAndGetTypePokemon(name: String): TypesRedis {
        val typeDetailData: TypeDetailData = jacksonObjectMapper().readValue(
            hitApiGet("${GlobalConstants.POKEMON_URL}/type/$name")
        )

        val pokemonWeakness = ArrayList<String>()
        val pokemonResistance = ArrayList<String>()

        typeDetailData.damageRelations?.doubleDamageFrom?.forEach { pokemonData ->
            pokemonData.name?.let { weaknessName -> pokemonWeakness.add(weaknessName.capitalized()) }
        }

        typeDetailData.damageRelations?.doubleDamageTo?.forEach { pokemonData ->
            pokemonData.name?.let { resistName -> pokemonResistance.add(resistName.capitalized()) }
        }

        val typesRedis = TypesRedis(
            name = typeDetailData.name?.capitalized(),
            resistance = pokemonResistance,
            weakness = pokemonWeakness
        )
        redisTemplate.storeAsValue(
            key = "${REDIS_KEY}_${SLUG_POKEMON_TYPE}_$name",
            value = typesRedis.toJson(),
            timeout = 12L to TimeUnit.HOURS
        )
        return typesRedis

    }

    companion object {
        private const val REDIS_KEY = RedisKey.TYPE_KEY
    }
}