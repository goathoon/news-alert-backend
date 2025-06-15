package com.newsalert.news_alert.rss.service

import com.newsalert.news_alert.rss.dto.Article
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URL
import org.springframework.stereotype.Service

@Service
class RssFetchService {

    fun testFetchSingleFeed() {
        val testFeed = "https://d2.naver.com/d2.atom"
        val articles = fetchFeed(testFeed)
        articles.forEach {
            println("📌 ${it.title} - ${it.link}")
        }
    }

    private fun fetchFeed(feedUrl: String): List<Article> {
        return try {
            val url = URL(feedUrl)
            val feed: SyndFeed = SyndFeedInput().build(XmlReader(url))
            feed.entries.map { entry ->
                Article(
                    title = entry.title,
                    link = entry.link,
                    summary = entry.description?.value ?: ""
                )
            }
        } catch (e: Exception) {
            println("❌ 오류 발생 [$feedUrl]: ${e.message}")
            emptyList()
        }
    }
}