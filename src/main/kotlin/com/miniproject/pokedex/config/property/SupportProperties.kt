package com.miniproject.pokedex.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "config")
data class SupportProperties(
	val name: String,
	val baseUrl: String,
	val defaultLanguage: String,
	val acceptedLanguage: List<String>,
	val defaultAccessKey: String,
	val redis: RedisProperties,
	val url: UrlProperties,
	val restTemplate: RestTemplateConfig,
)


data class RedisProperties(
	var host: String,
	var port: Int,
	var password: String,
	val database: RedisDatabaseProperties
)

data class RedisDatabaseProperties(
	var session: Int,
	var config: Int,
	var profile: Int
)

data class UrlProperties(
	var backofficeNewPassword: String
)

data class RestTemplateConfig(
	val connectionTimeout: String,
	val readTimeout: String
)

