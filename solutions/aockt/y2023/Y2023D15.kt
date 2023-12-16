package aockt.y2023

import aockt.y2023.Y2023D15.Operation.*
import io.github.jadarma.aockt.core.Solution


object Y2023D15 : Solution {
    override fun partOne(input: String): Any {
        return input.split(",").sumOf { it.hash() }
    }

    override fun partTwo(input: String): Any {
        val boxLine = BoxLine()
        val operations = input.asOperations()
        boxLine.run(operations)
        return boxLine.focusingPower()
    }

    private fun String.asOperations(): List<Operation> {
        return this.split(",").map { Operation.fromString(it) }
    }

    fun String.hash(): Int {
        return this.toCharArray().fold(0) { acc, c -> (acc + c.code) * 17 % 256 }
    }

    class BoxLine {
        private val boxes = List(256) { mutableListOf<Lens>() }

        private fun perform(operation: Operation) {
            val box = boxes[operation.box]
            when (operation) {
                is Remove -> {
                    box.removeAll { it.label == operation.label }
                }
                is Replace -> {
                    val oldLensIndex = box.indexOfFirst { it.label == operation.label }
                    if (oldLensIndex != -1) {
                        box.removeAt(oldLensIndex)
                        box.add(oldLensIndex, operation.lens)
                    } else {
                        box.add(operation.lens)
                    }
                }
            }
        }

        fun run(operations: List<Operation>) {
            for (operation in operations) {
                perform(operation)
            }
        }

        fun focusingPower(): Int {
            var result = 0
            for ((box, lenses) in boxes.withIndex()) {
                for ((slot, lens) in lenses.withIndex()) {
                    result += (box + 1) * (slot + 1) * lens.focalLength
                }
            }
            return result
        }
    }

    data class Lens(val label: String, val focalLength: Int)

    sealed class Operation {
        abstract val label: String

        val box: Int
            get() = label.hash()

        data class Replace(override val label: String, val focalLength: Int): Operation() {
            val lens: Lens
                get() = Lens(label, focalLength)
        }
        data class Remove(override val label: String): Operation()

        companion object {
            fun fromString(string: String): Operation {
                if (string.endsWith('-')) {
                    return Remove(string.removeSuffix("-"))
                } else {
                    val (label, focalLength) = string.split("=")
                    return Replace(label, focalLength.toInt())
                }
            }
        }
    }
}