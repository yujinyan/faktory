package me.yujinyan.faktory

import me.yujinyan.faktory.text.MarkovGenerator
import org.junit.jupiter.api.Test

class MarkovGeneratorTest {

    @Test
    fun `can read text from resources`() {
        val generator = MarkovGenerator(3).apply {
            feedTextResource("/factory/text/cn")
        }
        repeat(3) {
            generator.generate(60).also { println(it) }
        }
    }
}