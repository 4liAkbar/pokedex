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
    val pokemonService: PokemonService) : BaseController() {

    @GetMapping(value = ["pokemon", "pokemon/{name}"])
    fun getPokemon(
        @PathVariable(value = "name", required = false) name: String?,
        @RequestParam start: Int?, limit: Int?
    ): ResponseEntity<ResultResponse<Any>> = when (name) {
        null -> generateResponse(pokemonService.findAllPokemon(start, limit)).done()
        else -> generateResponse(pokemonService.findPokemonByName(name)).done()
    }
}