package aockt.y2023

import aockt.util.Grid
import aockt.util.Grid.Cell
import aockt.util.elementPairs
import io.github.jadarma.aockt.core.Solution
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Y2023D11 : Solution {
    override fun partOne(input: String): Any {
        val universe = input.asUniverse()
        universe.expand(2)
        val galaxies = universe.galaxies
        return galaxies.elementPairs().sumOf { entry -> universe.distance(entry.first.cell, entry.second.cell) }
    }

    override fun partTwo(input: String): Any {
        val universe = input.asUniverse()
        universe.expand(1000000)
        val galaxies = universe.galaxies
        return galaxies.elementPairs().sumOf { entry -> universe.distance(entry.first.cell, entry.second.cell) }
    }

    private fun String.asUniverse(): Universe = Universe.fromString(this)
}

private class Universe(private val space: Grid<Char>) {

    private val emptyRows: List<Int> = space.cellsByRows.emptySpaceIndexes()
    private val emptyCols: List<Int> = space.cellsByColumns.emptySpaceIndexes()
    private var expansionFactor: Long = 1

    val galaxies: List<Grid.Entry<Char>> = space.entries.filter { it.value == '#' }

    companion object {
        fun fromString(string: String): Universe {
            val space = Grid.fromString(string)
            return Universe(space)
        }
    }

    fun List<List<Cell>>.emptySpaceIndexes(): List<Int> = this.withIndex()
        .filter { (_, line) -> line.all { space[it] == '.' } }
        .map { it.index }

    fun distance(from: Cell, to: Cell): Long {
        fun emptyRowsInBetween(): Int {
            val a = min(from.y, to.y)
            val b = max(from.y, to.y)
            return emptyRows.count { it in a + 1..<b }
        }

        fun emptyColsInBetween(): Int {
            val a = min(from.x, to.x)
            val b = max(from.x, to.x)
            return emptyCols.count { it in a + 1..<b }
        }

        val extraSpaceDueToExpansion = (emptyRowsInBetween() + emptyColsInBetween()) * (expansionFactor - 1)

        return abs(to.x - from.x) + abs(to.y - from.y) + extraSpaceDueToExpansion
    }

    fun expand(factor: Int) {
        expansionFactor = factor.toLong()
    }
}