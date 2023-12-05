package aockt.y2023

import aockt.util.asIntListSeperatedBySpaces
import aockt.util.pow
import aockt.util.splitBySpaces
import io.github.jadarma.aockt.core.Solution

typealias CardDek = MutableList<Int>

object Y2023D04 : Solution {
    override fun partOne(input: String): Any {
        val scratchcards = input.asScratchCards()
        return scratchcards.sumOf(Scratchcard::points)
    }

    override fun partTwo(input: String): Any {
        val scratchcards = input.asScratchCards()
        val cardDeck = MutableList(scratchcards.size) { 1 }
        for (scratchcard in scratchcards) {
            updateDeckWithCard(cardDeck, scratchcard)
        }
        return cardDeck.totalCards()
    }

    private fun updateDeckWithCard(cardDeck: CardDek, card: Scratchcard) {
        val winCount = card.winCount
        val cardIndex = card.id - 1
        val copies = cardDeck[cardIndex]
        for (i in 1..winCount) {
            cardDeck[cardIndex + i] += copies
        }
    }

    private fun CardDek.totalCards(): Int = this.sum()

    private fun String.asScratchCards(): List<Scratchcard> {
        return this.lines().map(String::toScratchcard)
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
            val (cardInfoString, numberString) = cardString.split(regex = ": +".toRegex())
            val (_, cardIdString) = cardInfoString.splitBySpaces()
            return Scratchcard(
                id = cardIdString.toInt(),
                numbers = numberString.asIntListSeperatedBySpaces(),
                winningNumbers = winningNumberString.asIntListSeperatedBySpaces().toSet()
            )
        }
    }

    val points: Int
        get() {
            return when (val winningCount = winCount) {
                0 -> 0
                else -> 2.pow(winningCount - 1)
            }
        }

    val winCount: Int
        get() = numbers.count { it in winningNumbers }
}