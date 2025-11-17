package com.newsalert.rss.collector

import com.newsalert.common.logger
import com.newsalert.rss.article.Article
import com.newsalert.rss.article.ArticleRepository
import com.newsalert.rss.collector.config.RssProps
import com.newsalert.rss.collector.model.RawNewsEvent
import com.newsalert.rss.collector.service.NewsCollectService
import lombok.extern.slf4j.Slf4j
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NewsCollectScheduler(
    private val rssProps: RssProps,
    private val newsCollectService: NewsCollectService,
) {
    private val log by logger()

    @Scheduled(fixedDelay = 60_000)
    fun tick() {
        newsCollectService.collectFromUrls(rssProps.feeds)
    }
}
