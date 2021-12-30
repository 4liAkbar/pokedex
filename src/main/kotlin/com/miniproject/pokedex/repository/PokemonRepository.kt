package com.miniproject.pokedex.repository

import com.miniproject.pokedex.model.entity.PokemonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PokemonRepository : JpaRepository<PokemonEntity, Long> {

    fun findByNameOrId(name: String, id: Long): PokemonEntity?

}