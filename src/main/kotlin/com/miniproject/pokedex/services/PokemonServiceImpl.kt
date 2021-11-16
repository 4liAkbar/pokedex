package com.miniproject.pokedex.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.miniproject.pokedex.config.extension.capitalized
import com.miniproject.pokedex.config.extension.hitApiGet
import com.miniproject.pokedex.config.property.GlobalConstants.POKEMON_URL
import com.miniproject.pokedex.model.payload.PokemonDetailResponse
import com.miniproject.pokedex.model.payload.PokemonResponse
import com.miniproject.pokedex.model.payload.pokeapi.PokemonApiResponse
import com.miniproject.pokedex.model.payload.pokeapi.PokemonDetailData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.servlet.NoHandlerFoundException

@Service
class PokemonServiceImpl @Autowired constructor(
    private val typesService: TypesService
) : PokemonService {

    override fun findAllPokemon(start: Int?, limit: Int?): List<PokemonResponse> {
        val pokemonListResponse = ArrayList<PokemonResponse>()
        var pokemonId = 0
        val params = mapOf("offset" to start, "limit" to limit)
        val urlParams = params.map { (key, value) -> "${key}=${value}" }
            .joinToString("&")

        val listPokemon: PokemonApiResponse =
            jacksonObjectMapper().readValue(hitApiGet("$POKEMON_URL/pokemon?$urlParams"))

        if (listPokemon._results != null) {
            pokemonId = pokemonId.plus(
                when (start) {
                    null -> 1
                    else -> start
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
        var pokemonDetailResponse = PokemonDetailResponse()

        val apiDetailResponse = hitApiGet("$POKEMON_URL/pokemon/$name")

        if (apiDetailResponse != "Not Found") {
            val pokemonDetailData: PokemonDetailData =
                jacksonObjectMapper().readValue(apiDetailResponse)
            if (pokemonDetailData.id != null) {
                val pokemonDetailType = ArrayList<String>()
                var pokemonWeakness = ArrayList<String>()
                var pokemonResistance = ArrayList<String>()
                pokemonDetailData.types?.forEach {
                    it.type?.name?.let { typeName -> pokemonDetailType.add(typeName) }

                    val weaknessResist = it.type?.name?.let { it1 -> typesService.getPokemonType(it1) }
                    pokemonWeakness = weaknessResist?.weakness!!
                    pokemonResistance = weaknessResist?.resistance!!
                }

                pokemonDetailResponse = PokemonDetailResponse(
                    id = pokemonDetailData.id,
                    name = pokemonDetailData.name?.capitalized(),
                    type = pokemonDetailType,
                    sprite = pokemonDetailData.sprites?.frontDefault,
                    resistance = pokemonResistance,
                    weakness = pokemonWeakness,
                    description = "Height : ${pokemonDetailData._height}, Weight : ${pokemonDetailData._weight}"
                )
            }
        } else {
            throw NoHandlerFoundException("GET", "Pokemon $name", HttpHeaders.EMPTY)
        }

        return pokemonDetailResponse
    }


}