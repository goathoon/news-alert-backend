package com.newsalert.rss.collector.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "rss")
data class RssProps(
    val feeds: List<String>,
)
