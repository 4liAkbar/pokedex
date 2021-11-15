package com.miniproject.pokedex.config.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfiguration {
    fun generateJedisConnectionFactory(): JedisConnectionFactory {
        val standaloneConfiguration = RedisStandaloneConfiguration().apply {
            hostName = "127.0.0.1"
            port = 6379
            database = 1
        }
        return JedisConnectionFactory(standaloneConfiguration).apply {
            afterPropertiesSet()
        }
    }

    @Bean
    fun redisTemplate() = StringRedisTemplate().apply {
        setConnectionFactory(generateJedisConnectionFactory())
        hashKeySerializer = StringRedisSerializer()
        hashValueSerializer = Jackson2JsonRedisSerializer(Any::class.java)
    }
}
