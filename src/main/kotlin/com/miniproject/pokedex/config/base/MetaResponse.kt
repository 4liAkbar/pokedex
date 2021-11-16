package com.miniproject.pokedex.config.base

data class MetaResponse(
    var code: Int,
    var message: String? = "",
    var debugInfo: String? = ""
)