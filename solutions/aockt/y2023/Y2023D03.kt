package aockt.y2023

import aockt.util.Grid
import io.github.jadarma.aockt.core.Solution

object Y2023D03 : Solution {
    override fun partOne(input: String): Any {
        val schematic = Grid.fromString(input)
        val numbers = schematic.fetchNumbers()
        return numbers
            .filter { number -> number.isPartNumberOf(schematic) }
            .sumOf(NumberEntry::value)
    }
}

typealias Schematic = Grid<Char>

private fun Schematic.fetchNumbers(): List<NumberEntry> {
    return this.entries.flatMap { it.fetchNumbers() }
}

private fun List<SchematicEntry>.fetchNumbers(): List<NumberEntry> {
    val entries = mutableListOf<NumberEntry>()
    var startCell: Grid.Cell? = null
    var number = 0
    for ((index, entry) in this.withIndex()) {
        if (entry.isDigit()) {
            if (startCell == null) {
                startCell = entry.cell
            }
            number = number * 10 + entry.digitToInt()
            if (index == this.lastIndex) {
                entries.add(NumberEntry(value = number, start = startCell, end = entry.cell))
            }
        } else {
            if (number != 0 && startCell != null) {
                entries.add(NumberEntry(value = number, start = startCell, end = this[index - 1].cell))
            }
            number = 0
            startCell = null
        }
    }
    return entries
}

typealias SchematicEntry = Grid.Entry<Char>

private fun SchematicEntry.isDigit() = this.value.isDigit()
private fun SchematicEntry.digitToInt() = this.value.digitToInt()

private data class NumberEntry(val value: Int, val start: Grid.Cell, val end: Grid.Cell) {
    fun isPartNumberOf(schematic: Schematic): Boolean {
        return this.start.hasAdjacentSymbolIn(schematic) ||
                this.end.hasAdjacentSymbolIn(schematic)
    }

    private fun Grid.Cell.hasAdjacentSymbolIn(schematic: Schematic): Boolean {
        return schematic.adjacentCellsOf(this).any { schematic[it].isSymbol() }
    }

    private fun Char.isSymbol(): Boolean {
        return this != '.' && !this.isDigit()
    }
}


