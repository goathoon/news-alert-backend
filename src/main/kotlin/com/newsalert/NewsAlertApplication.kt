package com.newsalert

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewsAlertApplication

fun main(args: Array<String>) {
	runApplication<NewsAlertApplication>(*args)
}
