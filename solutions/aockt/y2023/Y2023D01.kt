package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D01 : Solution {
    override fun partOne(input: String): Any {
        return input.lines().sumOf { it.calibrationValue() }
    }

    private fun String.calibrationValue(): Int {
        val firstDigit = this.first() { it.isDigit() }.digitToInt()
        val lastDigit = this.last { it.isDigit() }.digitToInt()
        return firstDigit * 10 + lastDigit
    }
}