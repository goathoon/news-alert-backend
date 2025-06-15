package com.newsalert.news_alert.rss.controller

import com.newsalert.news_alert.rss.service.RssFetchService
import lombok.`val`
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RssFetchController (
    val rssFetchService: RssFetchService
){
    @GetMapping("/rss/fetch-test")
    fun fetchTest(): String {
        rssFetchService.testFetchSingleFeed()
        println()
        return "✅ RSS Fetch Completed - check logs"
    }
}