package com.miniproject.pokedex.config.extension

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.util.*

internal class AppExtTest {

    @Mock
    private lateinit var redisTemplate: StringRedisTemplate

    @Mock
    lateinit var valueOperations: ValueOperations<String, String>

    @Test
    fun testToJson() {
        val menu = mapOf(
            "menu" to "Espresso",
            "price" to 25000,
            "available" to true
        )

        val menuInJson: String = menu.toJson()
        val menuAfterParsing = ObjectMapper().readValue<Map<String, Any>>(menuInJson)

        assertNotNull(menuInJson)
        assertNotNull(menuAfterParsing)
        assertEquals(menuAfterParsing["menu"], "Espresso")
        assertEquals(menuAfterParsing["price"], 25000)
        assertEquals(menuAfterParsing["available"], true)
    }

    @Test
    fun testRedis() {
        val sampleKey = "backend:sample-key"
        val sampleValue = UUID.randomUUID().toString()

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        Mockito.doNothing().`when`(valueOperations).set(any(), any())
        Mockito.`when`(valueOperations.get(any())).thenReturn(sampleValue, null)
        Mockito.`when`(redisTemplate.delete(any<String>())).thenReturn(true)

        redisTemplate.storeAsValue(sampleKey, sampleValue)
        assertEquals(redisTemplate.getValue(sampleKey), sampleValue)

        redisTemplate.delete(sampleKey)
        assertNull(redisTemplate.getValue(sampleKey))
    }


}