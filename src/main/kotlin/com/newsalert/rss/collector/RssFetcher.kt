package com.newsalert.rss.collector

import com.newsalert.common.logger
import com.newsalert.rss.collector.model.RawNewsEvent
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import org.springframework.stereotype.Service
import java.net.URI

@Service
class RssFetcher {
    private val log by logger()

    fun fetchFrom(address: String): List<RawNewsEvent> {
        URI(address).toURL().openStream().use { inputStream ->
            val feed = SyndFeedInput().build(XmlReader(inputStream))
            return feed.entries.map {
                RawNewsEvent.from(it)
            }
        }
    }
}
