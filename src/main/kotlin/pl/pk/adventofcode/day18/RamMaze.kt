package pl.pk.adventofcode.day18

import java.util.PriorityQueue
import kotlin.math.abs

class RamMaze {

    companion object {

        fun getShortestPathLengthForBytes(input: String, fallenBytes: Int): Pair<Int, Int> {
            val positions = input.split("\n")
                .map {
                    val position = it.split(",").map { number -> number.toInt() }
                    Pair(position[0], position[1])
                }

            val map = List(71) { CharArray(71) }
            for (i in map.indices) {
                for (j in map[0].indices) {
                    map[i][j] = '.'
                }
            }

            positions.subList(0, fallenBytes).forEach { map[it.second][it.first] = '#' }

            // for part 1
//            return aStarShortestPath(map, Pair(0, 0), Pair(70, 70))

            // for part 2
            val remainingBytes = positions.subList(fallenBytes, positions.size)
            var checkedIndices = remainingBytes.indices.toList()
            var lastFoundByte: Pair<Int, Int> = Pair(-1, -1)
            while (checkedIndices.size != 1) {
                val mapCopy = List(71) { CharArray(71) }
                for (i in map.indices) {
                    for (j in map[i].indices) {
                        mapCopy[i][j] = map[i][j]
                    }
                }
                val cutOffIndex = checkedIndices[checkedIndices.size / 2]
                remainingBytes.subList(0, cutOffIndex + 1).forEach { mapCopy[it.second][it.first] = '#' }
                if (aStarShortestPath(mapCopy, Pair(0, 0), Pair(70, 70)) == Int.MAX_VALUE) {
                    checkedIndices = checkedIndices.subList(0, checkedIndices.size / 2)
                    lastFoundByte = remainingBytes[cutOffIndex]
                } else {
                    checkedIndices = checkedIndices.subList(checkedIndices.size / 2 + 1, checkedIndices.size)
                }
            }

            remainingBytes.subList(0, checkedIndices[0] + 1).forEach { map[it.second][it.first] = '#' }
            if (aStarShortestPath(map, Pair(0, 0), Pair(70, 70)) == Int.MAX_VALUE) {
                return remainingBytes[checkedIndices[0]]
            }

            return lastFoundByte
        }

        private fun aStarShortestPath(map: List<CharArray>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
            val possibleMoves = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
            val rows = map.size
            val columns = map[0].size
            val distances = mutableMapOf<Pair<Int, Int>, Int>()
            distances[start] = 0
            val nodesToVisitQueue = PriorityQueue<Pair<Int, Int>> { node1, node2 ->
                val node1Value = distances[node1]?.let { it + taxicabDistance(node1, end) } ?: Int.MAX_VALUE
                val node2Value = distances[node2]?.let { it + taxicabDistance(node2, end) } ?: Int.MAX_VALUE
                node1Value - node2Value
            }
            nodesToVisitQueue.add(start)

            while (nodesToVisitQueue.isNotEmpty()) {
                val currentNode = nodesToVisitQueue.poll()
                if (currentNode == end) {
                    return distances[currentNode]!!
                }

                for (move in possibleMoves) {
                    val newRowIndex = currentNode.first + move.first
                    val newColumnIndex = currentNode.second + move.second
                    if (newRowIndex in 0..<rows && newColumnIndex in 0..<columns && map[newRowIndex][newColumnIndex] == '.') {
                        val neighbour = Pair(newRowIndex, newColumnIndex)
                        val currentDistance = distances[currentNode]!!
                        if (currentDistance + 1 < distances.getOrDefault(neighbour, Int.MAX_VALUE)) {
                            distances[neighbour] = currentDistance + 1
                            nodesToVisitQueue.add(neighbour)
                        }
                    }
                }
            }

            return distances.getOrDefault(end, Int.MAX_VALUE)
        }

        private fun taxicabDistance(a: Pair<Int, Int>, b: Pair<Int, Int>) =
            abs(a.first - b.first) + abs(a.second - b.second)
    }
}