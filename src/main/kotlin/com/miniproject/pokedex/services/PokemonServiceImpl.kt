package com.miniproject.pokedex.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.miniproject.pokedex.config.exception.AppException
import com.miniproject.pokedex.config.exception.DataNotFoundException
import com.miniproject.pokedex.config.extension.capitalized
import com.miniproject.pokedex.config.extension.hitApiGet
import com.miniproject.pokedex.config.property.GlobalConstants.POKEMON_URL
import com.miniproject.pokedex.model.entity.PokemonEntity
import com.miniproject.pokedex.model.payload.PokemonDetailResponse
import com.miniproject.pokedex.model.payload.PokemonResponse
import com.miniproject.pokedex.model.payload.pokeapi.PokemonApiResponse
import com.miniproject.pokedex.model.payload.pokeapi.PokemonDetailData
import com.miniproject.pokedex.repository.PokemonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.collections.ArrayList

@Service
class PokemonServiceImpl @Autowired constructor(
    private val typesService: TypesService,
    private val pokemonRepository: PokemonRepository
) : PokemonService {

    override fun findAllPokemon(page: Int, limit: Int): List<PokemonResponse> {
        val pokemonListResponse = ArrayList<PokemonResponse>()
        var pokemonId = 0

        when{
            (page < 1) -> throw AppException("Parameter page can't below 1")
            (limit < 1) -> throw AppException("Parameter limit can't below 1")
        }

        val start = (page-1) * limit
        val params = mapOf("offset" to start, "limit" to limit)
        val urlParams = params.map { (key, value) -> "${key}=${value}" }
            .joinToString("&")

        val listPokemon: PokemonApiResponse =
            jacksonObjectMapper().readValue(hitApiGet("$POKEMON_URL/pokemon?$urlParams"))

        if (listPokemon._results != null) {
            pokemonId = pokemonId.plus(
                when {
                    (start <= 0) -> 1
                    else -> start+1
                }
            )

            listPokemon._results?.forEach {
                val pokemonResponse = PokemonResponse()
                pokemonResponse.name = it.name?.capitalized()
                pokemonResponse.id = pokemonId
                pokemonListResponse.add(pokemonResponse)
                pokemonId = pokemonId.plus(1)
            }
        }

        return pokemonListResponse
    }

    override fun findPokemonByName(name: String): PokemonDetailResponse {
        val pokemonDetailResponse = pokemonRepository.findByNameOrId(name, name.toLongOrNull() ?: 0)
        return when(pokemonDetailResponse){
            null ->  return savePokemon(name)
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
        val apiDetailResponse = hitApiGet("$POKEMON_URL/pokemon/$name")

        val pokemonDetailType = ArrayList<String>()
        var pokemonWeakness : List<String> = emptyList()
        var pokemonResistance : List<String> = emptyList()

        if (apiDetailResponse == "Not Found"){
            throw DataNotFoundException("Pokemon $name not found")
        }

        val pokemonDetailData = jacksonObjectMapper().readValue<PokemonDetailData>(apiDetailResponse)

        pokemonDetailData.types?.forEach {
            pokemonDetailType.add(it.type?.name.orEmpty().capitalized())

            val weaknessResist = typesService.getPokemonType(it.type?.name.orEmpty())
            if(weaknessResist != null) {
                pokemonWeakness = weaknessResist.weakness!!
                pokemonResistance = weaknessResist.resistance!!
            }
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
            pokemonRepository.save(PokemonEntity(
                id = it.id,
                name = it.name,
                description = it.description,
                sprite = it.sprite,
                type = pokemonDetailType.joinToString(),
                resistance = pokemonResistance.joinToString(),
                weakness = pokemonWeakness.joinToString()))
        }

        return pokemonDetailResponse
    }

}