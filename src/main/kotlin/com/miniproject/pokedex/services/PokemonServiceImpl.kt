package com.miniproject.pokedex.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.miniproject.pokedex.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import kotlin.collections.ArrayList

@Service
class PokemonServiceImpl @Autowired constructor() : PokemonService {

    private val pokeApiUrl : String = "https://pokeapi.co/api/v2"

    override fun findAllPokemon(start : Int?, limit : Int?) : List<PokemonResponse> {
        val pokemonListResponse = ArrayList<PokemonResponse>()
        var pokemonId = 0
        val params = mapOf("offset" to start, "limit" to limit)
        val urlParams = params.map {(key, value) -> "${key}=${value}"}
            .joinToString("&")
        val listPokemon : PokemonApiResponse = jacksonObjectMapper().readValue(hitApiPokemon("$pokeApiUrl/pokemon?$urlParams"))

        if(listPokemon._results?.size!! > 0){
            pokemonId = pokemonId.plus(when(start){null -> 1 else -> start+1})

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

    override fun findPokemonByName(name : String): PokemonDetailResponse {
        val pokemonDetailResponse = PokemonDetailResponse()
        val pokemonDetailData : PokemonDetailData = jacksonObjectMapper().readValue(hitApiPokemon("$pokeApiUrl/pokemon/$name"))
        if(pokemonDetailData.id != null){
            val pokemonDetailType = ArrayList<String>()
            val pokemonWeakness = ArrayList<String>()
            val pokemonResistance = ArrayList<String>()
            pokemonDetailData.types?.forEach { types ->
                types.type?.name?.let {typeName -> pokemonDetailType.add(typeName.capitalized())}

                val typeDetailData : TypeDetailData = jacksonObjectMapper().readValue(
                    hitApiPokemon("$pokeApiUrl/type/${types.type?.name}"))
                if(typeDetailData.name != null){
                    typeDetailData.damageRelations?.doubleDamageFrom?.forEach{ pokemonData ->
                        pokemonData.name?.let{ weaknessName -> pokemonWeakness.add(weaknessName.capitalized())}
                    }

                    typeDetailData.damageRelations?.doubleDamageTo?.forEach{ pokemonData ->
                        pokemonData.name?.let{ resistName -> pokemonResistance.add(resistName.capitalized())}
                    }
                }
            }

            pokemonDetailResponse.id = pokemonDetailData.id
            pokemonDetailResponse.name = pokemonDetailData.name?.capitalized()
            pokemonDetailResponse.type = pokemonDetailType
            pokemonDetailResponse.sprite = pokemonDetailData.sprites?.frontDefault
            pokemonDetailResponse.resistance = pokemonResistance
            pokemonDetailResponse.weakness = pokemonWeakness
            pokemonDetailResponse.description = "Height : ${pokemonDetailData._height}, Weight : ${pokemonDetailData._weight}"
        }

        return pokemonDetailResponse
    }

    private fun hitApiPokemon(apiUrl : String) : String{
        var result = ""
        try{
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build()

            val responseApi = client.send(request, HttpResponse.BodyHandlers.ofString())
            if(responseApi.statusCode()==200) result = responseApi.body()
        }catch (ex : Exception){
            println(ex.message)
        }

        return result
    }

    fun String.capitalized(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }

}