package aockt.y2023

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2023, 7, "Day 7: Camel Cards")
class Y2023D07Test : AdventSpec<Y2023D07>({
    partOne {
        """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent() shouldOutput 6440
    }

    partTwo {
        """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent() shouldOutput 5905
    }
})