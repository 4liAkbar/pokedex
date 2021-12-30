package com.miniproject.pokedex.controller

import com.miniproject.pokedex.config.base.BaseController
import com.miniproject.pokedex.config.base.ResultResponse
import com.miniproject.pokedex.services.PokemonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/ws"])
class PokemonController @Autowired constructor(
    val pokemonService: PokemonService
) : BaseController() {

    @GetMapping(value = ["pokemon"])
    fun getPokemonList(
        @RequestParam page: Int?, limit: Int?
    ): ResponseEntity<ResultResponse<Any>> {
        val response = pokemonService.findAllPokemon(page ?: 1, limit ?: 20)
        return generateResponse(response).done()
    }

    @GetMapping(value = ["pokemon/{name}"])
    fun getPokemonByName(
        @PathVariable("name") name: String,
    ): ResponseEntity<ResultResponse<Any>> {
        val response = pokemonService.findPokemonByName(name)
        return generateResponse(response).done()
    }

}