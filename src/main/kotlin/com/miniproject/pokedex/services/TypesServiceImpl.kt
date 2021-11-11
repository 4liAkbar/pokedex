package com.miniproject.pokedex.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
abstract class TypesServiceImpl @Autowired constructor(
    private val redisTemplate: StringRedisTemplate
    ) : TypesService {

}