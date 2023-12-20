package aockt.util

import kotlin.math.max

class SparseGrid<T> {
    private val data: MutableMap<Pair<Int, Int>, T> = mutableMapOf()
    var width: Int = 0
        private set

    var height: Int = 0
        private set

    fun addPoint(x: Int, y: Int, value: T) {
        data[x to y] = value
        width = max(width, x)
        height = max(height, y)
    }

    fun addPoint(pair: Pair<Int, Int>, value: T) {
        addPoint(pair.first, pair.second, value)
    }

    fun getPoint(x: Int, y: Int): T? = data[x to y]
    operator fun get(x: Int, y: Int): T? = data[x to y]

    fun count(predicate: (T) -> Boolean): Int = data.count { predicate(it.value) }
}