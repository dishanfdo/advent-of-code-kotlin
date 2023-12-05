package aockt.y2023

import aockt.util.asIntListSeperatedBySpaces
import aockt.util.pow
import io.github.jadarma.aockt.core.Solution

object Y2023D04 : Solution {
    override fun partOne(input: String): Any {
        val scratchcards = input.lines().map { it.toScratchcard() }
        return scratchcards.sumOf(Scratchcard::points)
    }
}

private fun String.toScratchcard(): Scratchcard {
    return Scratchcard.fromString(this)
}

data class Scratchcard(
    val id: Int,
    val numbers: List<Int>,
    val winningNumbers: Set<Int>
) {
    companion object {
        fun fromString(string: String): Scratchcard {
            val (cardString, winningNumberString) = string.split(" \\| +".toRegex())
            val (_, numberString) = cardString.split(regex = ": +".toRegex())
            return Scratchcard(
                id = 0, // TODO: Parse the id
                numbers = numberString.asIntListSeperatedBySpaces(),
                winningNumbers = winningNumberString.asIntListSeperatedBySpaces().toSet()
            )
        }
    }

    val points: Int
        get() {
            val winningCount = numbers.count { it in winningNumbers }
            return when (winningCount) {
                0 -> 0
                else -> 2.pow(winningCount - 1)
            }
        }
}