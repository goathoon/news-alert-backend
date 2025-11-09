package com.newsalert.rss.article

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface ArticleRepository : MongoRepository<Article, String> {
    fun findByLink(link: String): Article?
}
