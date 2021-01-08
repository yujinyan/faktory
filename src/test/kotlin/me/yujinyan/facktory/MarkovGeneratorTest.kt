package me.yujinyan.facktory

import me.yujinyan.facktory.text.MarkovGenerator
import org.junit.jupiter.api.Test

class MarkovGeneratorTest {

    @Test
    fun `can read text from resources`() {
        val generator = MarkovGenerator(3).apply {
            feedResourcesText("/factory/text/cn")
        }
        repeat(3) {
            generator.generate(60).also { println(it) }
        }
    }
}