package aockt.y2023

import aockt.util.Grid
import aockt.util.splitByEmptyLines
import io.github.jadarma.aockt.core.Solution
import java.util.Stack

object Y2023D13 : Solution {
    override fun partOne(input: String): Any {
        val patterns = input.asPatterns()
        for(pattern in patterns) {
            println(pattern.reflectionScore)
        }
        return patterns.sumOf { it.reflectionScore }
    }

    private fun String.asPatterns(): List<Pattern> {
        return this.splitByEmptyLines().map { Pattern.fromString(it) }
    }
}

private class Pattern(val grid: Grid<Char>) {
    companion object {
        fun fromString(string: String): Pattern {
            return Pattern(Grid.fromString(string))
        }
    }

    fun reflectionScore(grid: List<List<Char>>): Int {
        val possibleReflectionAxes = mutableListOf<Int>()
        for (i in 0 ..< grid.size - 2) {
            val col1 = grid[i]
            val col2 = grid[i + 1]
            if (col1 == col2) {
                possibleReflectionAxes.add(i)
            }
        }

        var score = 0
        for (axis in possibleReflectionAxes) {
            var lowAxis = axis
            var highAxis = axis + 1
            var hasReflection = true
            while (lowAxis >= 0 && highAxis < grid.size) {
                if (grid[lowAxis] != grid[highAxis]) {
                    hasReflection = false
                    break
                }
                lowAxis--
                highAxis++
            }
            if (hasReflection) {
                score += axis + 1
            }
        }

        return score
    }


    val reflectionScore: Int
        get() {
            val rowScore = reflectionScore(grid.cellsByRows.map { row -> row.map { cell -> grid[cell] } })
            val colScore = reflectionScore(grid.cellsByColumns.map { col -> col.map { cell -> grid[cell] } })
            return rowScore * 100 + colScore
        }
}

fun main() {
    Y2023D13.partOne("""
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
    """.trimIndent())
}