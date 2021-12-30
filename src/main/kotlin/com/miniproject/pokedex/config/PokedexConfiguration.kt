package com.miniproject.pokedex.config

import com.miniproject.pokedex.config.filter.HttpRequestInterceptor
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableAutoConfiguration
class PokedexConfiguration {

    @Bean
    fun webConfiguration(
        httpRequestInterceptor: HttpRequestInterceptor,
    ): WebMvcConfigurer = object : WebMvcConfigurer {
        override fun addInterceptors(registry: InterceptorRegistry) {
            with(registry) {
                addInterceptor(httpRequestInterceptor)
            }
        }
    }

}