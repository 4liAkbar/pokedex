package com.miniproject.pokedex.services

import com.miniproject.pokedex.model.TypeDetailData

interface TypesService {
    fun getAllTypes() : List<TypeDetailData>
}