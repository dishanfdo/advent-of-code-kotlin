package aockt.y2023

import aockt.util.Grid
import aockt.util.SparseGrid
import aockt.util.splitBySpaces
import aockt.y2023.Y2023D18.DigPlan.Direction
import aockt.y2023.Y2023D18.DigPlan.Direction.*
import aockt.y2023.Y2023D18.Ground.Feature.*
import io.github.jadarma.aockt.core.Solution
import kotlin.math.PI

private typealias Point = Pair<Int, Int>

object Y2023D18 : Solution {
    override fun partOne(input: String): Any {
        val digPlan = input.asDigPlan()
        val ground = Ground()
        ground.executeDigPlan(digPlan)
        return ground.pitVolume
    }

    private fun String.asDigPlan(): DigPlan = DigPlan.fromString(this)

    private data class DigPlan(val instructions: List<Instruction>) {
        enum class Direction {
            UP, DOWN, LEFT, RIGHT;

            companion object {
                fun fromString(string: String): Direction {
                    return when (string) {
                        "U" -> UP
                        "D" -> DOWN
                        "L" -> LEFT
                        "R" -> RIGHT
                        else -> throw IllegalArgumentException("Invalid direction: $string")
                    }
                }
            }
        }

        data class Instruction(val direction: Direction, val size: Int, val color: String) {
            companion object {
                fun fromString(string: String): Instruction {
                    val (dirStr, sizeStr, color) = string.splitBySpaces()
                    val direction = Direction.fromString(dirStr)
                    val size = sizeStr.toInt()
                    return Instruction(direction, size, color)
                }
            }
        }

        companion object {
            fun fromString(string: String): DigPlan {
                return DigPlan(string.lines().map(Instruction.Companion::fromString))
            }
        }

    }

    private class Ground {

        enum class Feature {
            EMPTY, PIT
        }

        val grid = SparseGrid<Feature>()
        var currentPosition: Point = 0 to 0
        val pitVolume: Int
            get() = grid.count { it == PIT }

        init {
            grid.addPoint(currentPosition, PIT)
        }

        fun executeDigPlan(digPlan: DigPlan) {
            for (instruction in digPlan.instructions) {
                executeInstruction(instruction)
            }
            digInterior()
        }

        private fun digInterior() {
            for (x in 0..<grid.width) {
                for (y in 0..<grid.height) {
                    if (isInteriorPoint(x, y)) {
                        grid.addPoint(x to y, PIT)
                    }
                }
            }
        }

        private fun isInteriorPoint(x: Int, y: Int): Boolean {
            TODO()
        }

        private fun executeInstruction(instruction: DigPlan.Instruction) {
            val points = points(currentPosition, instruction.direction, instruction.size)
            for (point in points) {
                grid.addPoint(point, PIT)
            }
            currentPosition = points.last()
        }

        fun points(from: Point, direction: Direction, size: Int): List<Point> {
            return buildList {
                when (direction) {
                    UP -> {
                        repeat(size) { i ->
                            add(from.first to from.second - (i + 1))
                        }
                    }
                    DOWN -> {
                        repeat(size) { i ->
                            add(from.first to from.second + (i + 1))
                        }
                    }
                    LEFT -> {
                        repeat(size) { i ->
                            add(from.first - (i + 1) to from.second)
                        }
                    }
                    RIGHT -> {
                        repeat(size) { i ->
                            add(from.first + (i + 1) to from.second)
                        }
                    }
                }
            }
        }

    }
}