package pl.pk.adventofcode.day20

import kotlin.math.abs

class Race {

    companion object {

        private val POSSIBLE_MOVES = setOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))

        fun countCheatsSavingAtLeastGivenTime(input: String, time: Int): Int {
            val map = parseInput(input)
            var start = Pair(-1, -1)
            var end = Pair(-1, -1)
            for (i in map.indices) {
                for (j in map[i].indices) {
                    if (map[i][j] == 'S') {
                        start = Pair(i, j)
                    }

                    if (map[i][j] == 'E') {
                        end = Pair(i, j)
                    }
                }
            }

            val (distances, previousNodes) = getDistancesToFinishAndPreviousNodes(map, start, end)
            val nodesOnPath = mutableListOf<Pair<Int, Int>>()
            var current: Pair<Int, Int>? = end
            while (current != null) {
                nodesOnPath.addLast(current)
                current = previousNodes[current]
            }

            var cheatSavingCorrectTimeCount = 0
            for (i in nodesOnPath.indices) {
                for (j in i + 1..<nodesOnPath.size) {
                    val firstNode = nodesOnPath[i]
                    val secondNode = nodesOnPath[j]
                    val taxicabDistance = taxicabDistance(firstNode, secondNode)
                    // for part 1 taxicabDistance == 2
                    if (taxicabDistance <= 20 && distances[firstNode]!! - distances[secondNode]!! - taxicabDistance >= time) {
                        cheatSavingCorrectTimeCount++
                    }
                }
            }

            return cheatSavingCorrectTimeCount
        }

        private fun getDistancesToFinishAndPreviousNodes(map: List<CharArray>, start: Pair<Int, Int>,
                                                         end: Pair<Int, Int>): DistancesAndPreviousNodes {
            val rows = map.size
            val columns = map[0].size
            val nodesStack = ArrayDeque<Pair<Int, Int>>()
            val visitedNodes = mutableSetOf<Pair<Int, Int>>()
            val distances = mutableMapOf<Pair<Int, Int>, Int>()
            val previousNodes = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
            distances[start] = 0
            nodesStack.addLast(start)
            while (nodesStack.isNotEmpty() && !visitedNodes.contains(end)) {
                val current = nodesStack.removeLast()
                visitedNodes.add(current)
                for (move in POSSIBLE_MOVES) {
                    val checkedNode = Pair(current.first + move.first, current.second + move.second)
                    if (isCorrectNode(checkedNode, rows, columns) && map[checkedNode.first][checkedNode.second] != '#'
                        && !visitedNodes.contains(checkedNode)) {
                        distances[checkedNode] = distances[current]!! + 1
                        previousNodes[checkedNode] = current
                        nodesStack.addLast(checkedNode)
                    }
                }
            }

            return DistancesAndPreviousNodes(distances, previousNodes)
        }

        private fun isCorrectNode(node: Pair<Int, Int>, rows: Int, columns: Int) =
            node.first in 0..<rows && node.second in 0..<columns

        private fun taxicabDistance(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)

        private fun parseInput(input: String): List<CharArray> = input.split("\n").map { it.toCharArray() }
    }

    private data class DistancesAndPreviousNodes(val distances: Map<Pair<Int, Int>, Int>,
                                                 val previousNodes: Map<Pair<Int, Int>, Pair<Int, Int>>)
}