package com.atomicrobot.carbon

import okio.buffer
import okio.source
import java.io.File
import java.nio.charset.Charset

object TestExtensions

@Throws(Exception::class)
fun String.loadResourceAsString(): String {
    val url = TestExtensions::class.java.getResource(this)
    val file = File(url.file)
    return file.source().buffer().readString(Charset.forName("UTF-8"))
}
