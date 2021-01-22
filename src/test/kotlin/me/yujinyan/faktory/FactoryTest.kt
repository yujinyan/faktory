package me.yujinyan.faktory

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeTypeOf
import org.junit.jupiter.api.Test

class FactoryTest {
    @Test
    fun `can make one object`() {
        val postFactory = factory<Post>()
        postFactory.make()
            .shouldBeTypeOf<Post>()
            .apply {
                title shouldNotBe ""
                body shouldNotBe ""
            }
    }

    @Test
    fun `can make multiple objects`() {
        val postFactory = factory<Post>()
        postFactory.make(3)
            .shouldHaveSize(3)
            .forEach {
                it.shouldBeInstanceOf<Post>()
            }
    }

    @Test
    fun `can customize on per factory basis`() {
        val factory = factory<Post> {
            Post::title by { "Hello World!" }
        }

        factory.make().title shouldBe "Hello World!"
        factory.make { Post::title by { "Bonjour!" } }
            .title shouldBe "Bonjour!"
    }

    @Test
    fun `can customize on per call basis`() {
        val postFactory = factory<Post>()

        postFactory.make {
            Post::title by { "Hello World!" }
        }.title shouldBe "Hello World!"

        postFactory.make {
            Post::title by { "Bonjour!" }
        }.title shouldBe "Bonjour!"

        postFactory.make(3) {
            Post::title by { "Hello World!" }
        }.shouldHaveSize(3).forEach {
            it.title shouldBe "Hello World!"
        }
    }

    @Test
    fun `can customize nested field`() {
        val postFactory = factory<Post>()

        postFactory.make {
            Post::author / Author::name by { "Peter Parker" }
        }.author.name shouldBe "Peter Parker"
    }
}