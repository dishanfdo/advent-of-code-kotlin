package aockt.y2023

import aockt.util.asLongList
import aockt.util.compose
import io.github.jadarma.aockt.core.Solution

object Y2023D05 : Solution {
    override fun partOne(input: String): Any {
        val almanac = input.asAlmanac()
        return almanac.minLocation()
    }

    override fun partTwo(input: String): Any {
        val almanac = input.asAlmanac()
        return almanac.minLocation(useSeedRanges = true)
    }

    private fun String.asAlmanac(): Almanac {
        return Almanac.fromString(this)
    }
}

private data class Almanac(val seeds: List<Long>, val maps: List<Map>) {
    val combinedMap: Map
        get() = maps.reduce(::compose)

    fun minLocation(useSeedRanges: Boolean = false): Long {
        if (useSeedRanges) {
            val combinedMap = combinedMap
            var minLocation = Long.MAX_VALUE
            for (i in seeds.indices step 2) {
                val start = seeds[i]
                val len = seeds[i + 1]
                for (seed in start ..< start + len) {
                    val location = combinedMap.map(seed)
                    if (location < minLocation) {
                        minLocation = location
                    }
                }
            }
            return minLocation
        } else {
            return seeds.minOf { combinedMap.map(it) }
        }
    }

    companion object {
        fun fromString(string: String): Almanac {
            val sections = string.split("\n\n", "\r\n\r\n")
            val seeds = sections[0].split(": ")[1].asLongList()
            val maps = sections.drop(1).map { Map.fromString(it) }
            return Almanac(seeds, maps)
        }
    }
}


@JvmInline
private value class Map(val map: (Long) -> Long) {

    data class Entry(val destStart: Long, val sourceStart: Long, val length: Long) {
        operator fun contains(value: Long): Boolean = sourceStart <= value && value < sourceStart + length

        fun map(value: Long): Long = destStart + (value - sourceStart)
    }

    companion object {
        fun fromString(string: String): Map {
            val entries = string.lines().drop(1).map(String::asLongList).map { Entry(it[0], it[1], it[2]) }
            return createMap(entries)
        }

        private fun createMap(entries: List<Entry>): Map {
            val map = { source: Long ->
                val matchingEntry = entries.firstOrNull { source in it }
                matchingEntry?.map(source) ?: source
            }
            return Map(map)
        }
    }
}

private fun compose(a: Map, b: Map): Map {
    return Map(a.map compose b.map)
}

