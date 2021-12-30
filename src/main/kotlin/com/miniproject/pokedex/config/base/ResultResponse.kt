package com.miniproject.pokedex.config.base

data class ResultResponse<T>(
    var code: Int,
    var message: String,
    var data: T? = null,
)