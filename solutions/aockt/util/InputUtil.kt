@file:Suppress("unused")

package aockt.util

fun String.asIntList(vararg delimiters: String, limit: Int = 0): List<Int> =
    this.split(*delimiters, limit = limit).map(String::toInt)

fun String.asIntList(regex: Regex, limit: Int = 0): List<Int> =
    this.split(regex, limit).map(String::toInt)

fun String.asIntListSeperatedBySpaces(): List<Int> = this.asIntList(regex = " +".toRegex())