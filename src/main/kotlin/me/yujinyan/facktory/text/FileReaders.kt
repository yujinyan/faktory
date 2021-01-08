package me.yujinyan.facktory.text

import java.io.File

internal fun readTextsUnderResourcePath(path: String) = sequence {
    File(javaClass.getResource(path).toURI()).walkTopDown()
        .filter { it.isFile }
        .forEach {
            yield(it.readText())
        }
}
