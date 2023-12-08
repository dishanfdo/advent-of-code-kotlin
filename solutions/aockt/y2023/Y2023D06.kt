package aockt.y2023

import aockt.util.product
import aockt.util.splitBySpaces
import io.github.jadarma.aockt.core.Solution
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object Y2023D06 : Solution {
    override fun partOne(input: String): Any {
        val races = input.asRaces()
        return races.map { it.waysToWinOptimized }.product()
    }

    override fun partTwo(input: String): Any {
        return input.asSingleRace().waysToWinOptimized
    }

    private fun String.asRaces(): List<Race> {
        val lines = this.lines()
        val times = lines[0].splitBySpaces().drop(1).map(String::toLong)
        val records = lines[1].splitBySpaces().drop(1).map(String::toLong)
        return times.zip(records).map { (time, record) -> Race(time, record) }
    }

    private fun String.asSingleRace(): Race {
        val lines = this.lines()
        val time = lines[0].split(":")[1].replace(" ", "").toLong()
        val record = lines[1].split(":")[1].replace(" ", "").toLong()
        return Race(time, record)
    }
}

private data class Race(val time: Long, val record: Long) {
    val waysToWin: Int
        get() = (1 ..< time).count { waitTime -> distanceFor(waitTime) > record }

    private fun distanceFor(waitTime: Long): Long {
        val speed = waitTime
        val remainTime = time - waitTime
        return speed * remainTime
    }

    // Derived from math formula
    val waysToWinOptimized: Long
        get() {
            val delta = sqrt((time * time - 4 * record).toDouble())
            val t1 = (time - delta) / 2.0
            val t2 = (time + delta) / 2.0
            val t1Upper = ceil(t1).toLong()
            val t2Lower = floor(t2).toLong()
            val t1Correction = if (t1 < t1Upper) 1L else 0L
            val t2Correction = if (t2Lower < t2) 1L else 0L
            return t2Lower - t1Upper - 1 + t1Correction + t2Correction
        }
}