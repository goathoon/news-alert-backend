package com.newsalert.rss.repository

import com.newsalert.rss.article.Article
import com.newsalert.rss.article.ArticleRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.repository.findByIdOrNull
import java.time.Instant

@DataMongoTest
class ArticleRepositoryTest(
    @Autowired private val articleRepository: ArticleRepository,
) : DescribeSpec({

        describe("ArticleRepository") {

            context("저장 동작 확인") {
                it("저장한 기사를 조회할 수 있다") {
                    val savedArticle =
                        articleRepository.save(
                            Article(
                                link = "https://example.com/news/1",
                                title = "첫 뉴스",
                                publishedAt = Instant.parse("2024-11-01T10:00:00Z"),
                                content = "news",
                            ),
                        )

                    val found =
                        articleRepository
                            .findByIdOrNull(savedArticle.id!!)
                    found!!.title shouldBe savedArticle.title
                }
            }

            context("문서를 저장했을 때") {
                beforeTest {
                    articleRepository.deleteAll()

                    articleRepository.save(
                        Article(
                            link = "https://example.com/news/1",
                            title = "첫 뉴스",
                            publishedAt = Instant.parse("2024-11-01T10:00:00Z"),
                            content = "news",
                        ),
                    )
                }

                it("link로 검색하면 저장한 문서를 찾아야 한다") {
                    val all = articleRepository.findAll()
                    val found = articleRepository.findByLink("https://example.com/news/1")

                    found.shouldNotBeNull()
                    found.id.shouldNotBeNull()
                    found.title shouldBe "첫 뉴스"
                    found.publishedAt shouldBe Instant.parse("2024-11-01T10:00:00Z")
                }
            }
        }
    })
