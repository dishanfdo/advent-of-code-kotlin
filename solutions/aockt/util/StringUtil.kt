@file:Suppress("unused")

package aockt.util

fun String.splitBySpaces(): List<String> = this.split(" +".toRegex())

fun String.countOf(char: Char): Int = this.count { it == char }