package com.miniproject.pokedex.services

import com.miniproject.pokedex.model.redis.TypesRedis

interface TypesService {
    fun getPokemonType(name: String): TypesRedis?
}