package aockt.y2023

import aockt.y2023.Y2023D01.calibrationValue
import io.github.jadarma.aockt.core.Solution
import java.lang.IllegalArgumentException

object Y2023D01 : Solution {
    override fun partOne(input: String): Any {
        return input.lines().sumOf { it.calibrationValue() }
    }

    override fun partTwo(input: String): Any {
        return input.lines().sumOf { it.correctedCalibrationValue() }
    }

    private fun String.calibrationValue(): Int {
        val firstDigit = this.first() { it.isDigit() }.digitToInt()
        val lastDigit = this.last { it.isDigit() }.digitToInt()
        return firstDigit * 10 + lastDigit
    }

    private fun String.correctedCalibrationValue(): Int {
        fun String.digitAtIndex(index: Int): Int? {
            val c = this[index]
            if (c.isDigit()) {
                return c.digitToInt()
            }
            return null
        }

        fun String.digitWordAtIndex(index: Int): Int? {
            val digitWords = listOf(
                "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
            )
            val subString = this.substring(index)
            for (i in digitWords.indices) {
                if (subString.startsWith(digitWords[i])) {
                    return i + 1
                }
            }
            return null
        }

        fun String.digitOrDigitWordAtIndex(index: Int): Int? {
            return digitAtIndex(index) ?: digitWordAtIndex(index)
        }


        fun String.firstDigit(): Int {
            for (i in this.indices) {
                val digit = this.digitOrDigitWordAtIndex(i)
                if (digit != null) {
                    return digit
                }
            }
            throw IllegalArgumentException("${this}: doesn't contain a digit")
        }

        fun String.lastDigit(): Int {
            for (i in this.indices.reversed()) {
                val digit = this.digitOrDigitWordAtIndex(i)
                if (digit != null) {
                    return digit
                }
            }
            throw IllegalArgumentException("${this}: doesn't contain a digit")
        }

        val firstDigit = this.firstDigit()
        val lastDigit = this.lastDigit()
        return firstDigit * 10 + lastDigit
    }


}