package pl.pk.adventofcode.day16

import pl.pk.adventofcode.day16.Maze.Direction.EAST
import pl.pk.adventofcode.day16.Maze.Direction.NORTH
import pl.pk.adventofcode.day16.Maze.Direction.SOUTH
import pl.pk.adventofcode.day16.Maze.Direction.WEST
import java.util.PriorityQueue
import kotlin.math.min

class Maze {

    companion object {

        private const val MOVE_COST = 1
        private const val TURN_COST = 1000

        fun getLowestRouteCost(input: String): Int {
            val map = parseInput(input)
            val fieldDistances = mutableMapOf<FieldWithDirection, Int>()
            var endPosition: Pair<Int, Int> = Pair(-1, -1)
            val fieldsPriorityQueue = PriorityQueue<FieldWithDirection>(compareBy { fieldDistances[it] ?: Int.MAX_VALUE })
            for (i in map.indices) {
                for (j in map[i].indices) {
                    val position = Pair(i, j)
                    if (map[i][j] == 'E') {
                        endPosition = position
                    } else if (map[i][j] == 'S') {
                        val startingFieldWithDirection = FieldWithDirection(position, EAST)
                        fieldDistances[startingFieldWithDirection] = 0
                        fieldsPriorityQueue.add(startingFieldWithDirection)
                    }
                }
            }

            val previousFields = mutableMapOf<FieldWithDirection, MutableList<FieldWithDirection>>()
            val visitedNodes = mutableSetOf<FieldWithDirection>()
            while (fieldsPriorityQueue.isNotEmpty()) {
                val fieldWithDirection = fieldsPriorityQueue.poll()
                val (position, direction) = fieldWithDirection
                if (fieldDistances.containsKey(fieldWithDirection) && !visitedNodes.contains(fieldWithDirection)) {
                    visitedNodes.add(fieldWithDirection)
                    val currentFieldCost = fieldDistances[fieldWithDirection]!!
                    val afterRightTurn = FieldWithDirection(position, direction.turnRight())
                    if (fieldDistances.getOrDefault(afterRightTurn, Int.MAX_VALUE) > currentFieldCost + TURN_COST) {
                        fieldDistances[afterRightTurn] = currentFieldCost + TURN_COST
                        fieldsPriorityQueue.add(afterRightTurn)
                        previousFields[afterRightTurn] = mutableListOf(fieldWithDirection)
                    } else if (fieldDistances.getOrDefault(afterRightTurn, Int.MAX_VALUE) == currentFieldCost + TURN_COST) {
                        previousFields.computeIfAbsent(afterRightTurn) { mutableListOf() }.add(fieldWithDirection)
                    }

                    val afterLeftTurn = FieldWithDirection(position, direction.turnLeft())
                    if (fieldDistances.getOrDefault(afterLeftTurn, Int.MAX_VALUE) > currentFieldCost + TURN_COST) {
                        fieldDistances[afterLeftTurn] = currentFieldCost + TURN_COST
                        fieldsPriorityQueue.add(afterLeftTurn)
                        previousFields[afterLeftTurn] = mutableListOf(fieldWithDirection)
                    } else if (fieldDistances.getOrDefault(afterLeftTurn, Int.MAX_VALUE) == currentFieldCost + TURN_COST) {
                        previousFields.computeIfAbsent(afterLeftTurn) { mutableListOf() }.add(fieldWithDirection)
                    }

                    val (rowChange, columnChange) = direction.getRowAndColumnChange()
                    if (map[position.first + rowChange][position.second + columnChange] == '.'
                        || map[position.first + rowChange][position.second + columnChange] == 'E') {
                        val afterMove =
                            FieldWithDirection(Pair(position.first + rowChange, position.second + columnChange), direction)
                        if (fieldDistances.getOrDefault(afterMove, Int.MAX_VALUE) > currentFieldCost + MOVE_COST) {
                            fieldDistances[afterMove] = currentFieldCost + MOVE_COST
                            fieldsPriorityQueue.add(afterMove)
                            previousFields[afterMove] = mutableListOf(fieldWithDirection)
                        } else if (fieldDistances.getOrDefault(afterMove, Int.MAX_VALUE) == currentFieldCost + MOVE_COST) {
                            previousFields.computeIfAbsent(afterMove) { mutableListOf() }.add(fieldWithDirection)
                        }
                    }
                }
            }

            val endEastCost = fieldDistances.getOrDefault(FieldWithDirection(endPosition, EAST), Int.MAX_VALUE)
            val endWestCost = fieldDistances.getOrDefault(FieldWithDirection(endPosition, WEST), Int.MAX_VALUE)
            val endNorthCost = fieldDistances.getOrDefault(FieldWithDirection(endPosition, NORTH), Int.MAX_VALUE)
            val endSouthCost = fieldDistances.getOrDefault(FieldWithDirection(endPosition, SOUTH), Int.MAX_VALUE)

            // for part 1
//            return min(min(endEastCost, endWestCost), min(endNorthCost, endSouthCost))

            // for part 2
            val minCost = min(min(endEastCost, endWestCost), min(endNorthCost, endSouthCost))
            val checkedFields = mutableSetOf<Pair<Int, Int>>()
            val pathsCheckingStack = ArrayDeque<FieldWithDirection>()
            if (endEastCost == minCost) {
                pathsCheckingStack.addLast(FieldWithDirection(endPosition, EAST))
            }
            if (endWestCost == minCost) {
                pathsCheckingStack.addLast(FieldWithDirection(endPosition, WEST))
            }
            if (endNorthCost == minCost) {
                pathsCheckingStack.addLast(FieldWithDirection(endPosition, NORTH))
            }
            if (endSouthCost == minCost) {
                pathsCheckingStack.addLast(FieldWithDirection(endPosition, SOUTH))
            }
            var fieldsCounter = 0
            while (pathsCheckingStack.isNotEmpty()) {
                val currentField = pathsCheckingStack.removeLast()
                if (!checkedFields.contains(currentField.position)) {
                    fieldsCounter++
                    checkedFields.add(currentField.position)
                }
                previousFields.getOrDefault(currentField, mutableListOf()).forEach { pathsCheckingStack.addLast(it) }
            }

            return fieldsCounter
        }

        private fun parseInput(input: String): List<CharArray> {
            return input.split("\n")
                .map { it.toCharArray() }
        }
    }

    private data class FieldWithDirection(val position: Pair<Int, Int>, val direction: Direction)

    private enum class Direction {
        EAST,
        WEST,
        NORTH,
        SOUTH;

        fun turnRight(): Direction {
            return when (this) {
                EAST -> SOUTH
                SOUTH -> WEST
                WEST -> NORTH
                NORTH -> EAST
            }
        }

        fun turnLeft(): Direction {
            return when (this) {
                EAST -> NORTH
                SOUTH -> EAST
                WEST -> SOUTH
                NORTH -> WEST
            }
        }

        fun getRowAndColumnChange(): Pair<Int, Int> {
            return when(this) {
                EAST -> Pair(0, 1)
                SOUTH -> Pair(1, 0)
                WEST -> Pair(0, -1)
                NORTH -> Pair(-1, 0)
            }
        }
    }
}