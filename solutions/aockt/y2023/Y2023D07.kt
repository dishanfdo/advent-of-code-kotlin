package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D07 : Solution {
    override fun partOne(input: String): Any {
        val hands = input.asHands()
        return hands
            .sorted()
            .totalWinnings()
    }

    override fun partTwo(input: String): Any {
        val hands = input.asHands()
        Hand.useJokerRule = true
        return hands
            .sorted()
            .also { for (h in it) { println(h) } }
            .totalWinnings()
    }

    private fun Iterable<Hand>.totalWinnings(): Int =
        this.withIndex().sumOf { (index, hand) -> (index + 1) * hand.bid }

    private fun String.asHands(): List<Hand> {
        return this.lines()
            .map { Hand.fromString(it) }
    }
}

private data class Hand(val cards: List<Card>, val bid: Int): Comparable<Hand> {

    companion object {
        var useJokerRule: Boolean = false

        fun fromString(string: String): Hand {
            val (cardsStr, bidStr) = string.split(" ")
            val cards = cardsStr.toCharArray().map { c ->  Card.fromChar(c)!! }
            val bid = bidStr.toInt()
            return Hand(cards, bid)
        }
    }

    override fun compareTo(other: Hand): Int {
        val typeComparison = this.type.compareTo(other.type)
        if (typeComparison != 0) {
            return typeComparison
        }

        for (i in this.cards.indices) {
            val c1 = this.cards[i]
            val c2 = other.cards[i]
            val cardComparison = c1.compare(c2)
            if (cardComparison != 0) {
                return cardComparison
            }
        }

        return 0
    }

    val type: Type
        get() {
            val groupedCards = cards.groupBy { it }.mapValues { it.value.size }
            return if (useJokerRule) {
                val grouped = groupedCards.toMutableMap()
                val jokerCount = grouped[Card.J] ?: 0
                grouped.remove(Card.J)

                val maxCard = grouped.maxByOrNull { it.value }?.key ?: Card.J
                grouped[maxCard] = (grouped[maxCard] ?: 0) + jokerCount
                typeFor(grouped.values.toList())
            } else {
                typeFor(groupedCards.values.toList())
            }
        }

    private fun typeFor(counts: List<Int>): Type {
        return when (counts.sorted()) {
            listOf(5) -> Type.FIVE_OF_A_KIND
            listOf(1, 4) -> Type.FOUR_OF_A_KIND
            listOf(2, 3) -> Type.FULL_HOUSE
            listOf(1, 1, 3) -> Type.THREE_OF_A_KIND
            listOf(1, 2, 2) -> Type.TWO_PAIR
            listOf(1, 1, 1, 2) -> Type.ONE_PAIR
            else -> Type.HIGH_CARD
        }
    }

    enum class Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND,
    }

    enum class Card(val char: Char): Comparable<Card> {
        TWO('2'),
        THREE('3'),
        FOUR('4'),
        FIVE('5'),
        SIX('6'),
        SEVEN('7'),
        EIGHT('8'),
        NINE('9'),
        T('T'),
        J('J'),
        Q('Q'),
        K('K'),
        A('A');

        companion object {
            fun fromChar(char: Char): Card? {
                return entries.firstOrNull { it.char == char }
            }
        }

        fun compare(other: Card): Int = if (useJokerRule) {
            this.compareWithJokerRule(other)
        } else {
            this.compareTo(other)
        }

        fun compareWithJokerRule(other: Card): Int {
            return when {
                this == J && other != J -> -1
                this != J && other == J -> 1
                else -> this.compareTo(other)
            }
        }
    }

}