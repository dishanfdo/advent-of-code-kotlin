@file:Suppress("unused")

package aockt.util

fun String.splitByEmptyLines() = this.split("\n\n", "\r\n\r\n")

fun String.asIntList(vararg delimiters: String, limit: Int = 0): List<Int> =
    this.split(*delimiters, limit = limit).map(String::toInt)

fun String.asIntList(regex: Regex = " +".toRegex(), limit: Int = 0): List<Int> =
    this.split(regex, limit).map(String::toInt)

fun String.asLongList(regex: Regex = " +".toRegex(), limit: Int = 0): List<Long> =
    this.split(regex, limit).map(String::toLong)

fun String.asIntListSeperatedBySpaces(): List<Int> = this.asIntList(regex = " +".toRegex())