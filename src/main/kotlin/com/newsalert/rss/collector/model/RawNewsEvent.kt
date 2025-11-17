package com.newsalert.rss.collector.model

import com.rometools.rome.feed.synd.SyndEntry
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

data class RawNewsEvent(
    val title: String,
    val content: String?,
    val url: String,
    val publishedAt: Instant?,
    val collectedAt: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")),
) {
    companion object {
        fun from(entry: SyndEntry): RawNewsEvent =
            RawNewsEvent(
                title = entry.title ?: "",
                content = entry.description?.value,
                url = entry.link,
                publishedAt = entry.publishedDate?.toInstant(),
            )
    }
}
