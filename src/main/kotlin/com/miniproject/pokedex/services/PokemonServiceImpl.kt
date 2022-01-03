package com.miniproject.pokedex.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.miniproject.pokedex.config.exception.AppException
import com.miniproject.pokedex.config.exception.DataNotFoundException
import com.miniproject.pokedex.config.extension.*
import com.miniproject.pokedex.config.property.GlobalConstants
import com.miniproject.pokedex.config.redis.RedisKey
import com.miniproject.pokedex.model.entity.PokemonEntity
import com.miniproject.pokedex.model.payload.PokemonDetailResponse
import com.miniproject.pokedex.model.payload.PokemonResponse
import com.miniproject.pokedex.model.payload.pokeapi.PokemonApiResponse
import com.miniproject.pokedex.model.payload.pokeapi.PokemonDetailData
import com.miniproject.pokedex.model.payload.pokeapi.TypeDetailData
import com.miniproject.pokedex.model.redis.TypesRedis
import com.miniproject.pokedex.repository.PokemonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class PokemonServiceImpl @Autowired constructor(
    private val pokemonRepository: PokemonRepository,
    private val redisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper
) : PokemonService {

    override fun findAllPokemon(page: Int, limit: Int): List<PokemonResponse> {
        val pokemonListResponse = ArrayList<PokemonResponse>()

        when {
            (page < 1) -> throw AppException("Parameter page can't below 1")
            (limit < 1) -> throw AppException("Parameter limit can't below 1")
        }

        val start = (page - 1) * limit
        val params = mapOf("offset" to start, "limit" to limit)
        val urlParams = params.map { (key, value) -> "${key}=${value}" }
            .joinToString("&")

        val listPokemon: PokemonApiResponse =
            objectMapper.readValue(hitApiGet("${GlobalConstants.POKEMON_URL}/pokemon?$urlParams"))
        var pokemonId = start + 1

        return when (listPokemon._results) {
            null -> pokemonListResponse
            else -> {
                listPokemon._results?.forEach {
                    pokemonListResponse.add(
                        PokemonResponse(
                            id = pokemonId,
                            name = it.name?.capitalized()
                        )
                    )
                    pokemonId = pokemonId.plus(1)
                }
                pokemonListResponse
            }
        }
    }

    override fun findPokemonByName(name: String): PokemonDetailResponse {
        return when (val pokemonDetailResponse = pokemonRepository.findByNameOrId(name, name.toLongOrNull() ?: 0)) {
            null -> return savePokemon(name)
            else -> PokemonDetailResponse(
                id = pokemonDetailResponse.id,
                name = pokemonDetailResponse.name,
                description = pokemonDetailResponse.description,
                sprite = pokemonDetailResponse.sprite,
                weakness = pokemonDetailResponse.weakness?.split(",")?.map { it.trim() },
                resistance = pokemonDetailResponse.resistance?.split(",")?.map { it.trim() },
                type = pokemonDetailResponse.type?.split(",")?.map { it.trim() }
            )
        }
    }

    private fun savePokemon(name: String): PokemonDetailResponse {
        val apiDetailResponse = hitApiGet("${GlobalConstants.POKEMON_URL}/pokemon/$name")

        val pokemonDetailType = ArrayList<String>()
        var pokemonWeakness: List<String> = emptyList()
        var pokemonResistance: List<String> = emptyList()

        if (apiDetailResponse == "Not Found") {
            throw DataNotFoundException("Pokemon $name not found")
        }

        val pokemonDetailData = objectMapper.readValue<PokemonDetailData>(apiDetailResponse)

        pokemonDetailData.types?.forEach {
            pokemonDetailType.add(it.type?.name.orEmpty().capitalized())

            val weaknessResist = getPokemonType(it.type?.name.orEmpty())
            pokemonWeakness = weaknessResist.weakness
            pokemonResistance = weaknessResist.resistance
        }

        val pokemonDetailResponse = PokemonDetailResponse(
            id = pokemonDetailData.id,
            name = pokemonDetailData.name?.capitalized(),
            type = pokemonDetailType,
            sprite = pokemonDetailData.sprites?.frontDefault,
            resistance = pokemonResistance,
            weakness = pokemonWeakness,
            description = "Height : ${pokemonDetailData._height}, Weight : ${pokemonDetailData._weight}"
        )
        pokemonDetailResponse.let {
            pokemonRepository.saveAndFlush(
                PokemonEntity(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    sprite = it.sprite,
                    type = pokemonDetailType.joinToString(),
                    resistance = pokemonResistance.joinToString(),
                    weakness = pokemonWeakness.joinToString()
                )
            )
        }

        return pokemonDetailResponse
    }

    private fun getPokemonType(name: String): TypesRedis {
        val data = when (val typesRedis =
            redisTemplate.getValue("${REDIS_KEY}_${GlobalConstants.SLUG_POKEMON_TYPE}_$name")) {
            null -> storeToRedisAndGetTypePokemon(name)
            else -> objectMapper.readValue(typesRedis)
        }

        return data
    }

    private fun storeToRedisAndGetTypePokemon(name: String): TypesRedis {
        val typeDetailData: TypeDetailData = objectMapper.readValue(
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
            key = "${REDIS_KEY}_${GlobalConstants.SLUG_POKEMON_TYPE}_$name",
            value = typesRedis.toJson(),
            timeout = 12L to TimeUnit.HOURS
        )
        return typesRedis

    }

    companion object {
        private const val REDIS_KEY = RedisKey.TYPE_KEY
    }
}