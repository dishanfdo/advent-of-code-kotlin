package aockt.util


@Suppress("unused", "MemberVisibilityCanBePrivate")
class Grid<T>(data: List<List<T>>) {
    private val data: MutableList<MutableList<T>> = data.map { it.toMutableList() }.toMutableList()

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

    val width: Int
        get() = data.firstOrNull()?.size ?: 0

    val height: Int
        get() = data.size

    fun adjacentCellsOf(cell: Cell, avoidDiagonals: Boolean = false): List<Cell> {
        val diffs = if (avoidDiagonals) {
            listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)
        } else {
            listOf(
                -1 to 1, 0 to 1, 1 to 1,
                -1 to 0, 1 to 0,
                -1 to -1, 0 to -1, 1 to -1
            )
        }
        val cells = diffs.map { (dx, dy) -> Cell(x = cell.x + dx, y = cell.y + dy) }
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

    val cellsByColumns: List<List<Cell>>
        get() {
            val cols = mutableListOf<List<Cell>>()
            for (x in 0..<width) {
                val col = mutableListOf<Cell>()
                for (y in 0..<height) {
                    col.add(Cell(x, y))
                }
                cols.add(col)
            }
            return cols
        }

    val cells: List<Cell>
        get() = cellsByRows.flatten()

    val entries: List<Entry<T>>
        get() = cells.map(::entry)

    val entriesByRows: List<List<Entry<T>>>
        get() = cellsByRows.map { row -> row.map(::entry) }

    fun <R> map(transform: (T) -> R): Grid<R> {
        return Grid(data.map { it.map(transform) })
    }

    fun first(value: T): Cell {
        return cells.first { this[it] == value }
    }

    fun addRow(index: Int, init: (Int) -> T) {
        data.add(index, MutableList(width, init))
    }

    fun addColumn(index: Int, init: (Int) -> T) {
        for ((row, rowData) in data.withIndex()) {
            rowData.add(index, init(row))
        }
    }
}
