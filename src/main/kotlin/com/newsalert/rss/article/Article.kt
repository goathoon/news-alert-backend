package com.newsalert.rss.article

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("articles")
data class Article(
    @Id
    var id: ObjectId? = null,
    @Indexed(unique = true)
    val link: String,
    val content: String?,
    val title: String,
    val publishedAt: Instant? = null,
)
