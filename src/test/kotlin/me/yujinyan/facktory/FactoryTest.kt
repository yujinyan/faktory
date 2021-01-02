package me.yujinyan.facktory

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
    fun `can customize on per call basis`() {
        val postFactory = factory<Post>()

        postFactory.make {
            Post::title by {"Hello World!"}
        }.title shouldBe "Hello World!"

        postFactory.make {
            Post::title by {"Bonjour!"}
        }.title shouldBe "Bonjour!"

        postFactory.make(3) {
            Post::title by {"Hello World!"}
        }.shouldHaveSize(3).forEach {
            it.title shouldBe "Hello World!"
        }
    }
}