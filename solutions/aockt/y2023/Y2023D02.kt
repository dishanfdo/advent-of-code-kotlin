package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D02 : Solution {
    override fun partOne(input: String): Any {
        val games = input.lines().map(Game::fromString)
        val bag = Bag(reds = 12, greens = 13, blues = 14)
        return games.sumOf { if (it.isValidGame(bag)) it.id else 0 }
    }

    override fun partTwo(input: String): Any {
        val games = input.lines().map(Game::fromString)
        return games.sumOf { it.powerOfMinimumSet() }
    }
}

data class Bag(val blues: Int, val greens: Int, val reds: Int)

data class Draw(val reds: Int, val greens: Int, val blues: Int) {
    fun isValidForBag(bag: Bag): Boolean {
        return this.reds <= bag.reds && this.greens <= bag.greens && this.blues <= bag.blues
    }

    companion object {
        fun fromString(string: String): Draw {
            val cubes = string.split(", ").map { it.split(" ") }
            var reds = 0
            var greens = 0
            var blues = 0
            for ((count, color) in cubes) {
                if (color == "red") {
                    reds = count.toInt()
                } else if (color == "green") {
                    greens = count.toInt()
                } else if (color == "blue") {
                    blues = count.toInt()
                }
            }
            return Draw(reds, greens, blues)
        }
    }
}

data class Game(val id: Int, val draws: List<Draw>) {

    fun isValidGame(bag: Bag): Boolean {
        return draws.all { it.isValidForBag(bag) }
    }

    fun powerOfMinimumSet(): Int {
        val minBag = minimumBag()
        return minBag.reds * minBag.greens * minBag.blues
    }

    private fun minimumBag(): Bag {
        val reds = draws.maxOfOrNull { it.reds } ?: 0
        val greens = draws.maxOfOrNull { it.greens } ?: 0
        val blues = draws.maxOfOrNull { it.blues } ?: 0
        return Bag(reds, greens, blues)
    }

    companion object {
        fun fromString(string: String): Game {
            val (gameInfo, drawsInfo) = string.split(": ")
            val id = gameInfo.substring(5).toInt()
            val draws = drawsInfo.split("; ").map(Draw::fromString)
            return Game(id, draws)
        }
    }
}