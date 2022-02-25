package com.bassmeister.burgerworld

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BurgerworldApplication

fun main(args: Array<String>) {
    runApplication<BurgerworldApplication>(*args)
}

