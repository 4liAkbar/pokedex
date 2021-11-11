package com.miniproject.pokedex

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAutoConfiguration
@EnableAsync
class PokedexApplication

fun main(args: Array<String>) {
	runApplication<PokedexApplication>(*args)
}

