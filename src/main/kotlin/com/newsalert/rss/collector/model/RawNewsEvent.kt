package com.newsalert.rss.collector.model

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

data class RawNewsEvent(
    val title: String,
    val content: String?,
    val url: String,
    val publishedAt: Instant?,
    val collectedAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
)