@file:Suppress("unused")

package aockt.util

fun List<Int>.product() = this.reduce { acc, i -> acc * i }
fun List<Long>.product() = this.reduce { acc, i -> acc * i }

fun <T> List<T>.elementPairs(): List<Pair<T, T>> {
    if (this.size < 2) return emptyList()

    val pairs = mutableListOf<Pair<T, T>>()
    for (i in this.indices) {
        for (j in i + 1..<this.size) {
            pairs.add(this[i] to this[j])
        }
    }
    return pairs
}