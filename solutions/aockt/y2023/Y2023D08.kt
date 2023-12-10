package aockt.y2023

import io.github.jadarma.aockt.core.Solution
import java.lang.IllegalArgumentException

object Y2023D08 : Solution {
    override fun partOne(input: String): Any {
        val (movesStr, networkStr) = input.split("\n\n", "\r\n\r\n")
        val moves = movesStr.asMoves()
        val network = networkStr.asNetwork()
        return network.stepCountForEndNode(moves)
    }

    private fun String.asMoves(): Moves = Moves.fromString(this)

    private fun String.asNetwork(): Network = Network.fromString(this)
}

private class Moves(private val moves: List<Move>) {
    private var nextMoveIndex = 0

    companion object {
        fun fromString(string: String): Moves {
            val moves = string.toCharArray().map { move ->
                when (move) {
                    'R' -> Move.RIGHT
                    'L' -> Move.LEFT
                    else -> throw IllegalArgumentException("Invalid move: $move")
                }
            }
            return Moves(moves)
        }
    }

    enum class Move {
        LEFT, RIGHT
    }

    fun nextMove(): Move {
        val move = moves[nextMoveIndex]
        nextMoveIndex = (nextMoveIndex + 1) % moves.size
        return move
    }
}

private class Network(private val nodes: Map<Node, Map<Moves.Move, Node>>) {

    companion object {
        fun fromString(string: String): Network {
            val nodes = string.lines().map { nodeEntry(it) }
            val nodeMap = mutableMapOf<Node, Map<Moves.Move, Node>>()
            for (entry in nodes) {
                nodeMap[entry.first] = entry.second
            }
            return Network(nodeMap)
        }

        private fun nodeEntry(string: String): Pair<Node, Map<Moves.Move, Node>> {
            val (source, destinationsStr) = string.split(" = ")
            val destinations = destinationsStr.drop(1).dropLast(1).split(", ")
            return Node(source) to mapOf(
                Moves.Move.LEFT to Node(destinations[0]),
                Moves.Move.RIGHT to Node(destinations[1]),
            )
        }
    }

    fun stepCountForEndNode(moves: Moves): Int {
        var count = 0
        var currentNode = Node("AAA")
        while (currentNode != Node("ZZZ")) {
            currentNode = node(from = currentNode, move = moves.nextMove())
            count++
        }
        return count
    }

    private fun node(from: Node, move: Moves.Move): Node = nodes[from]!![move]!!

    @JvmInline
    value class Node(val value: String)
}