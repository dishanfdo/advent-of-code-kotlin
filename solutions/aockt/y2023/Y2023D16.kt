package aockt.y2023

import aockt.util.Grid
import aockt.util.Grid.Cell
import aockt.y2023.Y2023D16.MirrorNetwork.Direction.*
import io.github.jadarma.aockt.core.Solution
import java.util.Stack


object Y2023D16 : Solution {
    override fun partOne(input: String): Any {
        val mirrorNetwork = input.asMirrorNetwork()
        return mirrorNetwork.energizedCellsForBeam(Cell(0, 0), WEST)
    }

    override fun partTwo(input: String): Any {
        val mirrorNetwork = input.asMirrorNetwork()
        return mirrorNetwork.maxPossibleEnergizedCells()
    }

    private fun String.asMirrorNetwork(): MirrorNetwork {
        return MirrorNetwork.fromString(this)
    }

    private class MirrorNetwork(private val grid: Grid<Char>) {
        companion object {
            fun fromString(string: String): MirrorNetwork {
                return MirrorNetwork(Grid.fromString(string))
            }
        }

        enum class Direction {
            NORTH, SOUTH, EAST, WEST
        }

        fun Cell.cellTo(direction: Direction): Cell {
            return when(direction) {
                NORTH -> Cell(this.x, this.y - 1)
                SOUTH -> Cell(this.x, this.y + 1)
                EAST -> Cell(this.x + 1, this.y)
                WEST -> Cell(this.x - 1, this.y)
            }
        }

        fun Cell.isValid(): Boolean = grid.isValidCell(this)

        fun Cell.nextCells(inDirection: Direction): List<Cell> {
            val element = grid[this]
            return when (element) {
                '.' -> when (inDirection) {
                    NORTH -> listOf(this.cellTo(SOUTH))
                    SOUTH -> listOf(this.cellTo(NORTH))
                    EAST -> listOf(this.cellTo(WEST))
                    WEST -> listOf(this.cellTo(EAST))
                }
                '/' -> when (inDirection) {
                    NORTH -> listOf(this.cellTo(WEST))
                    SOUTH -> listOf(this.cellTo(EAST))
                    EAST -> listOf(this.cellTo(SOUTH))
                    WEST -> listOf(this.cellTo(NORTH))
                }
                '\\' -> when (inDirection) {
                    NORTH -> listOf(this.cellTo(EAST))
                    SOUTH -> listOf(this.cellTo(WEST))
                    EAST -> listOf(this.cellTo(NORTH))
                    WEST -> listOf(this.cellTo(SOUTH))
                }
                '|' -> when (inDirection) {
                    NORTH -> listOf(this.cellTo(SOUTH))
                    SOUTH -> listOf(this.cellTo(NORTH))
                    EAST, WEST -> listOf(this.cellTo(NORTH), this.cellTo(SOUTH))
                }
                '-' -> when (inDirection) {
                    NORTH, SOUTH -> listOf(this.cellTo(WEST), this.cellTo(EAST))
                    EAST -> listOf(this.cellTo(WEST))
                    WEST -> listOf(this.cellTo(EAST))
                }

                else -> { throw IllegalArgumentException("Invalid element: $element") }
            }.filter { it.isValid() }
        }

        fun Cell.directionTo(other: Cell): Direction {
            val dx = other.x - this.x
            val dy = other.y - this.y
            return when (dx to dy) {
                1 to 0 -> WEST
                -1 to 0 -> EAST
                0 to 1 -> NORTH
                0 to -1 -> SOUTH
                else -> throw IllegalArgumentException("Invalid cell pair: $this & $other")
            }
        }

        fun energizedCellsForBeam(start: Cell, inDirection: Direction): Int {
            val energizedCells: MutableSet<Cell> = mutableSetOf()
            val visitedCells = mutableSetOf<Pair<Cell, Direction>>()
            val cellsToVisit = Stack<Pair<Cell, Direction>>()
            cellsToVisit.push(start to inDirection)
            while (cellsToVisit.isNotEmpty()) {
                val entry = cellsToVisit.pop()
                if (entry in visitedCells) { continue }

                val (cell, direction) = entry

                energizedCells.add(cell)
                visitedCells.add(entry)
                val nextCells = entry.first.nextCells(direction)
                for (next in nextCells) {
                    cellsToVisit.push(next to entry.first.directionTo(next))
                }
            }
            return energizedCells.size
        }

        fun maxPossibleEnergizedCells(): Int {
            val configurations = buildList {
                for (x in 0..<grid.width) {
                    add(Cell(x, 0) to NORTH)
                    add(Cell(x, grid.height - 1) to SOUTH)
                }

                for (y in 0..<grid.height) {
                    add(Cell(0, y) to WEST)
                    add(Cell(grid.width - 1, y) to EAST)
                }
            }

            return configurations.maxOf { (cell, direction) -> energizedCellsForBeam(cell, direction) }
        }
    }
}