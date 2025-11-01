package com.newsalert.rss.collector

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.time.ZoneId
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

class RssFetcherTest : StringSpec({

    lateinit var server: MockWebServer
    val rssFetcher = RssFetcher()

    beforeTest {
        server = MockWebServer()
        server.start()
    }

    afterTest { _ ->
        server.shutdown()
    }

    "RSS를 정상적으로 파싱해야 한다" {
        // given
        val rssXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <rss version="2.0">
              <channel>
                <title>테스트 피드</title>
                <item>
                  <title>첫 번째 뉴스</title>
                  <link>https://example.com/news/1</link>
                  <description>첫 번째 내용</description>
                  <pubDate>Mon, 01 Nov 2025 08:00:00 +0900</pubDate>
                </item>
                <item>
                  <title>두 번째 뉴스</title>
                  <link>https://example.com/news/2</link>
                  <description>두 번째 내용</description>
                  <pubDate>Mon, 01 Nov 2025 09:00:00 +0900</pubDate>
                </item>
              </channel>
            </rss>
        """.trimIndent()

        // mock 서버에 응답 1개 넣어둠
        server.enqueue(
            MockResponse()
                .setBody(rssXml)
                .addHeader("Content-Type", "application/rss+xml; charset=UTF-8")
        )

        // when
        val url = server.url("/rss").toString()
        val result = rssFetcher.fetchFrom(url)

        // then
        result shouldHaveSize 2
        result[0].title shouldBe "첫 번째 뉴스"
        result[0].collectedAt.zone shouldBe ZoneId.of("Asia/Seoul")
        result[0].url shouldBe "https://example.com/news/1"
        result[1].title shouldBe "두 번째 뉴스"
        println(result)
    }
})