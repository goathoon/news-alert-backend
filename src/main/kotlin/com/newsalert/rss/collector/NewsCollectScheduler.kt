package com.newsalert.rss.collector

import com.newsalert.common.logger
import com.newsalert.rss.article.Article
import com.newsalert.rss.article.ArticleRepository
import com.newsalert.rss.collector.config.RssProps
import lombok.extern.slf4j.Slf4j
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Slf4j
class NewsCollectScheduler(
    private val rssFetcher: RssFetcher,
    private val articleRepo: ArticleRepository,
    private val rssProps: RssProps,
) {
    private val log by logger()

    @Scheduled(fixedDelay = 60_000)
    fun tick() {
        rssProps.feeds.forEach { url ->
            // Fetch RSS
            val items =
                runCatching { rssFetcher.fetchFrom(url) }
                    .onFailure { log.warn("❌ RSS fetch 실패 | feed={} | msg={}", url, it.message) }
                    .getOrElse { return@forEach }

            // 저장
            items.forEach { item ->
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
                    log.warn("❌ 저장 실패 | feed={} | link={} | msg={}", url, item.url, e.message)
                }
            }

            // Fetch 성공 로그만 남김
            log.info("✅ RSS fetch 성공 | feed={} | count={}", url, items.size)
        }
    }
}
