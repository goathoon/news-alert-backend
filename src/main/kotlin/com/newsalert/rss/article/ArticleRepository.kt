package com.newsalert.rss.article

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ArticleRepository : MongoRepository<Article, ObjectId> {
    fun findByLink(link: String): Article?
}
