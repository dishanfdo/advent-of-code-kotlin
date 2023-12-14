package aockt.y2023

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2023, 13, "Day 13: Point of Incidence")
class Y2023D13Test : AdventSpec<Y2023D13>({
    partOne {
        """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
            
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent() shouldOutput 405
    }

    test("debug") {
        solution.partOne(
            """
                ##..####..###
                .#..####..#..
                #...#..#...##
                ##.#....#.###
                ...#.##.#.#..
                .###....###..
                #.########.##
                #..##..##..##
                .#.#....#.#..   
            """.trimIndent()
        )
    }

})