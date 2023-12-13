package aockt.y2023

import aockt.util.Grid
import io.github.jadarma.aockt.core.Solution

object Y2023D10 : Solution {
    override fun partOne(input: String): Any {
        val pipeNetwork = input.asPipeNetwork()
        return pipeNetwork.maxDistanceFromStart()
    }

    override fun partTwo(input: String): Any {
        val pipeNetwork = input.asPipeNetwork()
        return pipeNetwork.tilesInsidePipeLoop()
    }

    private fun String.asPipeNetwork(): PipeNetwork = PipeNetwork.fromString(this)
}

private class PipeNetwork(
    private val grid: Grid<Char>
) {
    companion object {
        fun fromString(string: String): PipeNetwork {
            val grid = Grid.fromString(string)
            return PipeNetwork(grid)
        }
    }

    private val pipeLoop = findPipeLoop()
    private val pipeLoopCells = pipeLoop.toSet()
    private val start = grid.first('S')
    private val startCellType: Char by lazy {
        val leftConnected = grid.left(start)?.let { grid[it] }?.canConnectToRight() ?: false
        val rightConnected = grid.right(start)?.let { grid[it] }?.canConnectToLeft() ?: false
        val upConnected = grid.up(start)?.let { grid[it] }?.canConnectToDown() ?: false
        val downConnected = grid.down(start)?.let { grid[it] }?.canConnectToUp() ?: false
        when {
            leftConnected && upConnected -> 'J'
            leftConnected && rightConnected -> '-'
            leftConnected && downConnected -> '7'
            upConnected && rightConnected -> 'L'
            upConnected && downConnected -> '|'
            rightConnected && downConnected -> 'F'
            else -> throw IllegalArgumentException("Invalid start cell configuration")
        }
    }

    fun maxDistanceFromStart(): Int = pipeLoop.size / 2

    fun tilesInsidePipeLoop(): Int {
        var tilesInsideLoop = 0
        for (cell in grid.cells) {
            if (cell.isInPipeLoop()) {
                tilesInsideLoop++
            }
        }
        return tilesInsideLoop
    }

    fun findPipeLoop(): List<Grid.Cell> {
        val start = grid.first('S')
        var previous = start
        var next = grid.adjacentCellsOf(start).first { it.canConnectTo(start) }
        val loop = mutableListOf<Grid.Cell>()
        loop.add(start)
        while (next != start) {
            loop.add(next)
            val new = next.nextConnectedCell(skip = previous)
            previous = next
            next = new
        }
        return loop
    }

    private fun Grid.Cell.canConnectTo(other: Grid.Cell): Boolean {
        val dx = other.x - this.x
        val dy = other.y - this.y
        return when (dx to dy) {
            0 to 1 -> grid[this].canConnectToDown() && grid[other].canConnectToUp()
            1 to 0 -> grid[this].canConnectToRight() && grid[other].canConnectToLeft()
            -1 to 0 -> grid[this].canConnectToLeft() && grid[other].canConnectToRight()
            0 to -1 -> grid[this].canConnectToUp() && grid[other].canConnectToDown()
            else -> false
        }
    }

    private fun Grid.Cell.nextConnectedCell(skip: Grid.Cell): Grid.Cell {
        return grid.adjacentCellsOf(this, avoidDiagonals = true)
            .first { it != skip && it.canConnectTo(this) }
    }

    private fun Grid.Cell.isInPipeLoop(): Boolean {
        // Using the method https://en.wikipedia.org/wiki/Point_in_polygon
        // tracing a ray in the positive x direction and count intersections with path
        if (this in pipeLoopCells) {
            return false
        }

        var borderCrossings = 0
        for (x in this.x ..< grid.width) {
            val cell = Grid.Cell(x, this.y)
            if (cell in pipeLoopCells && grid[cell].isHorizontalRayCrossingCell()) {
                borderCrossings++
            }
        }
        return borderCrossings % 2 == 1
    }

    private fun Char.canConnectToDown(): Boolean {
        return this in setOf('F', '|', '7', 'S')
    }

    private fun Char.canConnectToUp(): Boolean {
        return this in setOf('J', '|', 'L', 'S')
    }

    private fun Char.canConnectToRight(): Boolean {
        return this in setOf('F', '-', 'L', 'S')
    }

    private fun Char.canConnectToLeft(): Boolean {
        return this in setOf('7', '-', 'J', 'S')
    }

    private fun Char.isHorizontalRayCrossingCell(): Boolean {
        val c = if (this == 'S') startCellType else this
        return c in setOf('|', '7', 'F')
    }

    private fun <T> Grid<T>.right(cell: Grid.Cell): Grid.Cell? {
        if (cell.x >= this.width - 1) return null
        return Grid.Cell(cell.x + 1, cell.y)
    }

    private fun <T> Grid<T>.left(cell: Grid.Cell): Grid.Cell? {
        if (cell.x < 1) return null
        return Grid.Cell(cell.x - 1, cell.y)
    }

    private fun <T> Grid<T>.up(cell: Grid.Cell): Grid.Cell? {
        if (cell.y < 1) return null
        return Grid.Cell(cell.x, cell.y - 1)
    }

    private fun <T> Grid<T>.down(cell: Grid.Cell): Grid.Cell? {
        if (cell.y >= this.height - 1) return null
        return Grid.Cell(cell.x, cell.y + 1)
    }
}