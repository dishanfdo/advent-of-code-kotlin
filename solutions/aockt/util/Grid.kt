package aockt.util


@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Grid<T>(val data: List<List<T>>) {
    companion object {
        fun fromString(string: String): Grid<Char> {
            return Grid(data = string.lines().map { it.toCharArray().toList() })
        }
    }

    data class Cell(val x: Int, val y: Int)

    data class Entry<T>(val cell: Cell, val value: T)

    operator fun get(x: Int, y: Int): T = data[y][x]
    operator fun get(cell: Cell): T = data[cell.y][cell.x]
    fun entry(cell: Cell): Entry<T> = Entry(cell = cell, value = this[cell])

    val width = data.firstOrNull()?.size ?: 0
    val height = data.size

    fun adjacentCellsOf(cell: Cell): List<Cell> {
        val xDiffs = listOf(-1, 0, 1)
        val yDiffs = listOf(-1, 0, 1)
        val cells = xDiffs.flatMap { dx ->
            yDiffs.map { dy ->
                Cell(x = cell.x + dx, y = cell.y + dy)
            }
        }
        return cells.filter { it.isValid() }
    }

    private fun Cell.isValid(): Boolean = this.x in 0..<width && this.y in 0..<height

    val cellsByRows: List<List<Cell>>
        get() {
            val rows = mutableListOf<List<Cell>>()
            for (y in 0..<height) {
                val row = mutableListOf<Cell>()
                for (x in 0..<width) {
                    row.add(Cell(x, y))
                }
                rows.add(row)
            }
            return rows
        }

    val cells: List<Cell>
        get() = cellsByRows.flatten()

    val entries: List<Entry<T>>
        get() = cells.map(::entry)

    val entriesByRows: List<List<Entry<T>>>
        get() = cellsByRows.map { row -> row.map(::entry) }
}