package com.miniproject.pokedex.config.extension

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.redis.core.StringRedisTemplate
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import java.util.concurrent.TimeUnit


fun Any.toJson(): String = jacksonObjectMapper()
    .registerModule(KotlinModule())
    .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    .writeValueAsString(this)

fun StringRedisTemplate.getValue(key: String): String? = this.opsForValue().get(key)

fun StringRedisTemplate.storeAsValue(key: String, value: String, timeout: Pair<Long, TimeUnit>? = null) {
    with(this) {
        opsForValue().set(key, value)
        timeout?.let { expire(key, timeout.first, timeout.second) }
    }
}

fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}

fun hitApiGet(apiUrl: String): String {
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder()
        .uri(URI.create(apiUrl))
        .build()

    val responseApi = client.send(request, HttpResponse.BodyHandlers.ofString())
    return responseApi.body()
}