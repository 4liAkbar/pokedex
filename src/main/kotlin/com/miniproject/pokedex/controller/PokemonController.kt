package com.miniproject.pokedex.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.miniproject.pokedex.config.base.BaseController
import com.miniproject.pokedex.config.base.ResultResponse
import com.miniproject.pokedex.model.PokemonApiResponse
import com.miniproject.pokedex.model.PokemonData
import com.miniproject.pokedex.model.PokemonDetailData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@RestController
@RequestMapping(value = ["/ws"])
class PokemonController @Autowired constructor(
) : BaseController() {

    val pokeApiUrl : String = "https://pokeapi.co/api/v2/pokemon"

    @GetMapping(value = ["pokemon", "pokemon/{name}"])
    fun getPokemon(
        @PathVariable(value = "name", required = false) name: String?
    ): ResponseEntity<ResultResponse<Any>> = when (name) {
        null -> generateResponse(findAllPokemon()).done()
        else -> generateResponse(findPokemonByName(name)).done()
    }

    fun findAllPokemon() : PokemonApiResponse {
        return jacksonObjectMapper().readValue(hitApiPokemon(pokeApiUrl))
    }

    fun findPokemonByName(name : String): PokemonDetailData {
        return jacksonObjectMapper().readValue(hitApiPokemon("$pokeApiUrl/$name"))
    }

    fun hitApiPokemon(apiUrl : String) : String{
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .build()

        val responseApi = client.send(request, HttpResponse.BodyHandlers.ofString())
        return responseApi.body()
    }

}