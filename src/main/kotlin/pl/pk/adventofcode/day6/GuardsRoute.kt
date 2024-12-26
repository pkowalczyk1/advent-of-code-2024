package pl.pk.adventofcode.day6

import pl.pk.adventofcode.day6.GuardsRoute.Direction.UP

class GuardsRoute {

    companion object {

        fun countNewObstaclesToCreateLoop(input: String): Int {
            val map = parseInput(input)
            var guardPosition = Pair(-1, -1)
            val newObstaclePositions = mutableSetOf<Pair<Int, Int>>()
            for (i in map.indices) {
                for (j in map[i].indices) {
                    if (map[i][j] == '^') {
                        map[i][j] = '.'
                        guardPosition = Pair(i, j)
                    } else if (map[i][j] == '.') {
                        newObstaclePositions.add(Pair(i, j))
                    }
                }
            }

            return newObstaclePositions.count {
                map[it.first][it.second] = '#'
                val isLoop = isLoop(map, guardPosition)
                map[it.first][it.second] = '.'
                isLoop
            }
        }

        // for part 2
        private fun isLoop(map: List<CharArray>, initialPosition: Pair<Int, Int>): Boolean {
            val rows = map.size
            val columns = map[0].size
            val visitedFields = mutableSetOf<FieldWithDirection>()
            var guardPosition = initialPosition
            var direction = UP
            while (isCorrectIndex(guardPosition.first, rows) && isCorrectIndex(guardPosition.second, columns)) {
                val fieldWithDirection = FieldWithDirection(guardPosition, direction)
                if (visitedFields.contains(fieldWithDirection)) {
                    return true
                }

                visitedFields.add(fieldWithDirection)
                val newRow = guardPosition.first + direction.rowChange
                val newColumn = guardPosition.second + direction.columnChange
                if (!isCorrectIndex(newRow, rows) || !isCorrectIndex(newColumn, columns) || map[newRow][newColumn] == '.') {
                    guardPosition = Pair(newRow, newColumn)
                } else if (map[newRow][newColumn] == '#') {
                    direction = direction.turnRight()
                }
            }

            return false
        }

        // for part 1
        fun countFieldsVisitedByGuard(input: String): Int {
            val map = parseInput(input)
            val rows = map.size
            val columns = map[0].size
            var guardPosition = Pair(-1, -1)
            var direction = UP
            for (i in map.indices) {
                for (j in map[i].indices) {
                    if (map[i][j] == '^') {
                        map[i][j] = '.'
                        guardPosition = Pair(i, j)
                    }
                }
            }

            val visitedFields = mutableSetOf<Pair<Int, Int>>()
            while (isCorrectIndex(guardPosition.first, rows) && isCorrectIndex(guardPosition.second, columns)) {
                visitedFields.add(guardPosition)
                val newRow = guardPosition.first + direction.rowChange
                val newColumn = guardPosition.second + direction.columnChange
                if (!isCorrectIndex(newRow, rows) || !isCorrectIndex(newColumn, columns) || map[newRow][newColumn] == '.') {
                    guardPosition = Pair(newRow, newColumn)
                } else if (map[newRow][newColumn] == '#') {
                    direction = direction.turnRight()
                }
            }

            return visitedFields.size
        }

        private fun isCorrectIndex(index: Int, size: Int) = index in 0..<size

        private fun parseInput(input: String) = input.lines().map { it.toCharArray() }
    }

    private data class FieldWithDirection(val field: Pair<Int, Int>, val direction: Direction)

    private enum class Direction(val rowChange: Int, val columnChange: Int) {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        fun turnRight() =
            when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
    }
}