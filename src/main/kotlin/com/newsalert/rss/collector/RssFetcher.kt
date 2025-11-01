package com.newsalert.rss.collector

import com.newsalert.rss.collector.model.RawNewsEvent
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URI
import org.springframework.stereotype.Service

@Service
class RssFetcher {
    fun fetchFrom(address: String): List<RawNewsEvent> {
        URI(address).toURL().openStream().use { inputStream ->
            val feed = SyndFeedInput().build(XmlReader(inputStream))
            return feed.entries.map {
                RawNewsEvent(
                    title = it.title ?: "",
                    content = it.description?.value,
                    url = it.link,
                    publishedAt = it.publishedDate?.toInstant()
                )
            }
        }
    }
}