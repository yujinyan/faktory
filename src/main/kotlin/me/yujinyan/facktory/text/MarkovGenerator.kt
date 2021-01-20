package me.yujinyan.facktory.text

import kotlin.random.Random

public fun markovGenerator(n: Int): MarkovGenerator = object : MarkovGenerator(n) {}

public open class MarkovGenerator(private val n: Int) {
    private val ngramStore = mutableMapOf<String, MutableList<String>>()

    public fun feed(text: String) {
        val ngramSequence = text.splitToSequence("")
            .filterNot { it.isBlank() }
            .windowed(n) { it.joinToString("") }
        ngramSequence.windowed(2)
            .forEach {
                val prefix = it[0]
                val suffix = it[1].last()
                ngramStore.getOrPut(prefix) { mutableListOf() }.add(suffix.toString())
            }
    }

    public fun feedTextResource(path: String): Unit = this::class.java
        .getResourceAsStream(path)
        .reader()
        .readText().let { feed(it) }

    public fun generate(length: Int): String = buildString {
        val keys = ngramStore.keys.toList()
        var cur = keys[Random.nextInt(0, ngramStore.size - 1)].also { append(it) }
        var size = 0
        while (size < length) {
            size += n
            cur = cur.drop(1) + ngramStore[cur]!!.random().also { append(it) }
        }
    }
}
