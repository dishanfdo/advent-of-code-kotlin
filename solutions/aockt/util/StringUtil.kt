@file:Suppress("unused")

package aockt.util

fun String.splitBySpaces(): List<String> = this.split(" +".toRegex())