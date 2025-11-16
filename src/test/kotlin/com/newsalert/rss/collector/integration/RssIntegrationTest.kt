package com.newsalert.rss.collector.integration

import com.newsalert.rss.article.ArticleRepository
import com.newsalert.rss.collector.RssFetcher
import com.newsalert.rss.collector.model.RawNewsEvent
import com.newsalert.rss.collector.service.NewsCollectService
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.time.Instant

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = ["spring.task.scheduling.enabled=false"])
class RssIntegrationTest : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension) // 🔥 이거 추가

    @Autowired
    lateinit var newsCollectService: NewsCollectService

    @Autowired
    lateinit var articleRepo: ArticleRepository

    @MockkBean
    lateinit var rssFetcher: RssFetcher

    init {
        beforeTest {
            articleRepo.deleteAll()
        }

        describe("RSS 수집 서비스") {

            context("정상적인 RSS 응답을 받을 때") {
                it("MongoDB에 기사들을 저장해야 한다") {
                    // given
                    val feedUrl = "https://feed-1"

                    val news1 =
                        RawNewsEvent(
                            title = "첫 번째 뉴스",
                            url = "https://example.com/news/1",
                            content = "첫 번째 내용",
                            publishedAt = Instant.parse("2025-11-01T08:00:00Z"),
                        )

                    val news2 =
                        RawNewsEvent(
                            title = "두 번째 뉴스",
                            url = "https://example.com/news/2",
                            content = "두 번째 내용",
                            publishedAt = Instant.parse("2025-11-01T09:00:00Z"),
                        )

                    every { rssFetcher.fetchFrom(feedUrl) } returns
                        listOf(
                            news1,
                            news2,
                        )

                    // when
                    newsCollectService.collectFromUrls(listOf(feedUrl))

                    // then
                    val articles = articleRepo.findAll()
                    articles.size shouldBe 2
                    articles[0].link shouldBe "https://example.com/news/1"
                    articles[1].link shouldBe "https://example.com/news/2"
                }
            }

            context("같은 기사 링크로 중복된 수집 요청을 보내면") {
                it("하나만 저장한다") {
                    // given
                    val feedUrl = "https://feed-1"

                    val duplicatedEvents =
                        listOf(
                            RawNewsEvent(
                                title = "중복 뉴스 1",
                                url = "https://example.com/news/dup",
                                content = "내용 1",
                                publishedAt = Instant.parse("2025-11-01T08:00:00Z"),
                            ),
                            RawNewsEvent(
                                title = "중복 뉴스 2(동일 링크)",
                                url = "https://example.com/news/dup", // 👈 같은 링크
                                content = "내용 2",
                                publishedAt = Instant.parse("2025-11-01T09:00:00Z"),
                            ),
                        )
                    every { rssFetcher.fetchFrom(feedUrl) } returns duplicatedEvents

                    // when: 같은 feed 한 번 수집
                    newsCollectService.collectFromUrls(listOf(feedUrl))

                    // then
                    val articles = articleRepo.findAll()
                    articles.size shouldBe 1
                    articles[0].link shouldBe "https://example.com/news/dup"
                }
            }
        }
    }
}
