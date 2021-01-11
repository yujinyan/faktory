package me.yujinyan.facktory.text

import java.io.File

internal fun readTextResource(path: String) = sequence {
    val resource = javaClass.getResource(path)
        ?: throw IllegalArgumentException("No resource found under $path")

    File(resource.toURI()).walkTopDown()
        .filter { it.isFile }
        .forEach {
            yield(it.readText())
        }
}
