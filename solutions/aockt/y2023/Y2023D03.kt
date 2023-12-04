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

    override fun partTwo(input: String): Any {
        val schematic = Grid.fromString(input)
        return schematic.fetchGears().sumOf(Gear::gearRatio)
    }
}

typealias Schematic = Grid<Char>

private fun Schematic.fetchNumbers(): List<NumberEntry> {
    return this.entriesByRows.flatMap { it.fetchNumbers() }
}

private fun Schematic.fetchGears(): List<Gear> {
    val numbers = fetchNumbers()
    val candidateGearSymbols = numbers.fold(mutableMapOf<SchematicEntry, MutableSet<NumberEntry>>()) { acc, numberEntry ->
        val engineSymbolEntries = numberEntry.adjacentEngineSymbols(this)
        for (engineSymbolEntry in engineSymbolEntries) {
            if (engineSymbolEntry in acc) {
                acc[engineSymbolEntry]?.add(numberEntry)
            } else {
                acc[engineSymbolEntry] = mutableSetOf(numberEntry)
            }
        }
        acc
    }
    val gearSymbols = candidateGearSymbols.filterValues { it.size == 2 } // Gears have two part numbers
    return gearSymbols.map { (symbol, parts) ->
        val (part1, part2) = parts.toList().map(NumberEntry::value)
        Gear(part1 = part1, part2 = part2, cell = symbol.cell)
    }
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
private fun SchematicEntry.isGearSymbol() = this.value == '*'

private data class NumberEntry(val value: Int, val start: Grid.Cell, val end: Grid.Cell) {
    fun isPartNumberOf(schematic: Schematic): Boolean {
        return this.start.hasAdjacentSymbolIn(schematic) ||
                this.end.hasAdjacentSymbolIn(schematic)
    }

    fun adjacentEngineSymbols(schematic: Schematic): Set<SchematicEntry> {
        return this.start.adjacentEngineSymbolEntries(schematic) +
                this.end.adjacentEngineSymbolEntries(schematic)
    }

    private fun Grid.Cell.hasAdjacentSymbolIn(schematic: Schematic): Boolean {
        return schematic.adjacentCellsOf(this).any { schematic[it].isSymbol() }
    }

    private fun Grid.Cell.adjacentEngineSymbolEntries(schematic: Schematic): Set<SchematicEntry> {
        return schematic
            .adjacentCellsOf(this).map { schematic.entry(it) }
            .filter { it.isGearSymbol() }
            .toSet()
    }

    private fun Char.isSymbol(): Boolean {
        return this != '.' && !this.isDigit()
    }
}

private data class Gear(val part1: Int, val part2: Int, val cell: Grid.Cell) {
    val gearRatio
        get() = part1 * part2
}


