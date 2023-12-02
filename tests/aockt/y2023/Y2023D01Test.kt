package aockt.y2023

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2023, 1, "Question 1")
class Y2023D01Test : AdventSpec<Y2023D01>({
    partOne {
        """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent() shouldOutput 142
    }
})