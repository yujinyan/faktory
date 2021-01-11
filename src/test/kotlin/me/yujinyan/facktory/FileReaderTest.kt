package me.yujinyan.facktory

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.sequences.shouldContain
import io.kotest.matchers.sequences.shouldHaveSize
import io.kotest.matchers.string.shouldContain
import me.yujinyan.facktory.text.readTextResource
import org.junit.jupiter.api.Test

class FileReaderTest {
    @Test
    fun `can read path`() {
        readTextResource("/factory/text/en")
            .shouldContain("Hello World!")
    }

    @Test
    fun `can read path with trailing slash`() {
        readTextResource("/factory/text/en/")
            .shouldContain("Hello World!")
    }

    @Test
    fun `can read file`() {
        readTextResource("/factory/text/en/Hello.txt")
            .apply {
                shouldHaveSize(1)
                shouldContain("Hello World!")
            }
    }

    @Test
    fun `throws exception if provided non-existent path`() {
        shouldThrow<IllegalArgumentException> {
            readTextResource("/does/not/exist").first()
        }.message shouldContain "/does/not/exist"
    }
}
