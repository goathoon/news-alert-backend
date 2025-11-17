package com.newsalert.rss.collector.service

import com.newsalert.common.logger
import com.newsalert.rss.article.Article
import com.newsalert.rss.article.ArticleRepository
import com.newsalert.rss.collector.RssFetcher
import com.newsalert.rss.collector.model.RawNewsEvent
import org.springframework.stereotype.Service

@Service
class NewsCollectService(
    private val rssFetcher: RssFetcher,
    private val articleRepo: ArticleRepository,
) {
    private val log by logger()

    fun collectFromUrls(feeds: List<String>) {
        feeds.forEach { url ->
            collectFromUrl(url)
        }
    }

    fun collectFromUrl(url: String) {
        // Fetch RSS
        val items =
            runCatching { rssFetcher.fetchFrom(url) }
                .onFailure { log.warn("❌ RSS fetch 실패 | feed={} | msg={}", url, it.message) }
                .getOrElse { return }

        // 저장
        items.forEach { item ->
            saveArticleIfAbsentFrom(url, item)
        }

        // Fetch 성공 로그만 남김
        log.info("✅ RSS fetch 성공 | feed={} | count={}", url, items.size)
    }

    private fun saveArticleIfAbsentFrom(
        url: String,
        item: RawNewsEvent,
    ) {
        if (articleRepo.findByLink(item.url) == null) {
            runCatching {
                log.info("content = {}", item.content)
                articleRepo.save(
                    Article(
                        link = item.url,
                        title = item.title,
                        content = item.content,
                        publishedAt = item.publishedAt,
                    ),
                )
            }.onFailure { e ->
                log.warn(
                    "❌ 저장 실패 | feed={} | link={} | msg={}",
                    url,
                    item.url,
                    e.message,
                )
            }
        }
    }
}
