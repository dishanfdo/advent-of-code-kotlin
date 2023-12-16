package aockt.y2023

import aockt.util.Grid
import io.github.jadarma.aockt.core.Solution

object Y2023D14 : Solution {
    override fun partOne(input: String): Any {
        val panel = input.asPanel()
        panel.print()
        panel.tiltNorth()
        println()
        panel.print()
        return panel.totalLoadOnNorthBean()
    }

    override fun partTwo(input: String): Any {
        val panel = input.asPanel()
        return panel.loadOnNorthBeamAfterCycle(1000000000)
    }

    private fun String.asPanel(): Panel = Panel.fromString(this)
}

private class Panel(private var grid: Grid<Char>) {

    companion object {
        fun fromString(string: String): Panel {
            return Panel(Grid.fromString(string))
        }
    }

    fun loadOnNorthBeamAfterCycle(n: Int): Int {
        val nextGridCache: MutableMap<String, String> = mutableMapOf()
        val gridChain: MutableList<Pair<String, Int>> = mutableListOf()
        gridChain.add(this.code to this.totalLoadOnNorthBean())
        println("Initial: code: $code, load: ${totalLoadOnNorthBean()})")
        this.print()
        println("-----------------------------------------\n")
        repeat(n) {
            val key = this.code
            val nextGridCode = nextGridCache[key]
            if (nextGridCode == null) {
                cycle()
                println("After Cycle: ${it + 1} - code: $code, load: ${totalLoadOnNorthBean()}")
                this.print()
                println("-----------------------------------------\n")
                val currentCode = this.code
                nextGridCache[key] = currentCode
                if (nextGridCache.containsKey(currentCode)) {
                    // We've got a loop, can calculate the final value without iteration
                    val loopStart = gridChain.indexOfFirst { it.first == currentCode }
                    val a = loopStart - 1
                    val r = gridChain.size - loopStart
                    val i = (n - a) % r + a
                    return gridChain[i].second
                } else {
                    gridChain.add(currentCode to this.totalLoadOnNorthBean())
                }
            }
        }
        return totalLoadOnNorthBean()
    }

    fun cycle() {
        tiltNorth()
        tiltWest()
        tiltSouth()
        tiltEast()
    }

    fun tiltNorth() {
        for (x in 0..<grid.width) {
            for (y in 0..<grid.height) {
                if (grid[x, y] == 'O') {
                    var z = y - 1
                    while (z >= 0 && grid[x, z] == '.') {
                        z--
                    }
                    z++
                    if (z != y) {
                        grid[x, z] = 'O'
                        grid[x, y] = '.'
                    }
                }
            }
        }
    }

    fun tiltSouth() {
        for (x in 0..<grid.width) {
            for (y in (0..<grid.height).reversed()) {
                if (grid[x, y] == 'O') {
                    var z = y + 1
                    while (z < grid.height && grid[x, z] == '.') {
                        z++
                    }
                    z--
                    if (z != y) {
                        grid[x, z] = 'O'
                        grid[x, y] = '.'
                    }
                }
            }
        }
    }

    fun tiltWest() {
        for (y in (0..<grid.height)) {
            for (x in 0..<grid.width) {
                if (grid[x, y] == 'O') {
                    var z = x - 1
                    while (z >= 0 && grid[z, y] == '.') {
                        z--
                    }
                    z++
                    if (z != x) {
                        grid[z, y] = 'O'
                        grid[x, y] = '.'
                    }
                }
            }
        }
    }

    fun tiltEast() {
        for (y in 0..<grid.height) {
            for (x in (0..<grid.width).reversed()) {
                if (grid[x, y] == 'O') {
                    var z = x + 1
                    while (z < grid.width && grid[z, y] == '.') {
                        z++
                    }
                    z--
                    if (z != x) {
                        grid[z, y] = 'O'
                        grid[x, y] = '.'
                    }
                }
            }
        }
    }

    val code: String
        get() {
            return buildString {
                for (x in 0..<grid.width) {
                    for (y in 0..<grid.height) {
                        append(grid[x, y])
                    }
                }
            }
        }

    fun totalLoadOnNorthBean(): Int {
        val roundRocks = grid.entries.filter { it.value == 'O' }.map { it.cell }
        return roundRocks.sumOf { it.cost() }
    }

    fun Grid.Cell.cost(): Int {
        return grid.height - this.y
    }

    fun print() {
        grid.print()
    }
}