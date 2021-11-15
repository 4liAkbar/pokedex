package com.miniproject.pokedex.config.redis

class RedisKey {
    companion object {
        private const val REDIS_PREFIX = "pokedex"
        const val TYPE_KEY = "${REDIS_PREFIX}:type"
    }
}