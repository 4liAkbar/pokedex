package com.miniproject.pokedex.model.redis

import org.springframework.data.redis.core.RedisHash

@RedisHash("Type")
data class TypesRedis(
    val name: String? = null,
    val weakness: ArrayList<String>? = null,
    val resistance: ArrayList<String>? = null
)