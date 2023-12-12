package aockt.y2023

import aockt.util.asIntList
import io.github.jadarma.aockt.core.Solution

object Y2023D09 : Solution {
    override fun partOne(input: String): Any {
        return input.lines().sumOf { it.asIntList().nextValue() }
    }

    override fun partTwo(input: String): Any {
        return input.lines().sumOf { it.asIntList().previousValue() }
    }
}

private fun List<Int>.nextValue(): Int {
    var nextValue = 0
    var next = this
    while (next.last() != 0) {
        nextValue += next.last()
        next = next.nextSequence()
    }
    return nextValue
}

private fun List<Int>.previousValue(): Int {
    var nextValue = 0
    var next = this
    var sign = 1
    while (next.last() != 0) {
        nextValue += next.first() * sign
        sign *= -1
        next = next.nextSequence()
    }
    return nextValue
}

private fun List<Int>.nextSequence(): List<Int> {
    return this.zipWithNext { a, b -> b - a }
}