package aockt.y2023

import aockt.util.asIntList
import aockt.util.countOf
import aockt.util.splitBySpaces
import io.github.jadarma.aockt.core.Solution

object Y2023D12 : Solution {
    override fun partOne(input: String): Any {
        val records = input.asRecords()
        return records.sumOf { it.possibleArrangements() }
    }

    private fun String.asRecords(): List<Record> {
        return this.lines().map { Record.fromString(it) }
    }
}

private data class Record(val springs: String, val groups: List<Int>) {
    companion object {
        fun fromString(string: String): Record {
            val (springs, groupsStr) = string.splitBySpaces()
            val groups = groupsStr.asIntList(",")
            return Record(springs, groups)
        }
    }

    fun possibleArrangements(): Int {
        return allArrangements(springs).count { isValidArrangement(it) }
    }

    fun allArrangements(springs: String): List<String> {
        if (springs.countOf('?') == 0) {
            return listOf(springs)
        }
        return allArrangements(springs.replaceFirst('?', '.')) +
                allArrangements(springs.replaceFirst('?', '#'))
    }

    fun isValidArrangement(springs: String): Boolean {
        val groups = springs.split("\\.+".toRegex()).filter { it.isNotEmpty() }.map { it.length }
        return this.groups == groups
    }


}