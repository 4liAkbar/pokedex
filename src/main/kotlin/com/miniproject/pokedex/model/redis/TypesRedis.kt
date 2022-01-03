package com.miniproject.pokedex.model.redis

import org.springframework.data.redis.core.RedisHash

@RedisHash("Type")
data class TypesRedis(
    val name: String? = null,
    val weakness: List<String> = emptyList(),
    val resistance: List<String> = emptyList()
)