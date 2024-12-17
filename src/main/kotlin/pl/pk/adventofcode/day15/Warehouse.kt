package pl.pk.adventofcode.day15

class Warehouse {

    companion object {

        fun getBoxesCoordinatesSum(input: String): Int {
            val (map, moves) = parseInput(input)
            val largerMap = prepareLargerMap(map)
            var robotPosition = findRobot(largerMap)

            moves.forEach {
                when (it) {
                    '^' -> robotPosition = makeUpOrDownMove(largerMap, robotPosition, -1)
                    'v' -> robotPosition = makeUpOrDownMove(largerMap, robotPosition, 1)
                    '>' -> robotPosition = makeLeftOrRightMove(largerMap, robotPosition, 1)
                    '<' -> robotPosition = makeLeftOrRightMove(largerMap, robotPosition, -1)
                }
            }

            return calculateLargerCoordinatesFinalSum(largerMap)
        }

        // for part 1
        private fun makeMove(map: List<CharArray>, robotPosition: Pair<Int, Int>, rowChange: Int,
                             columnChange: Int): Pair<Int, Int> {
            val toMoveStack = ArrayDeque<Pair<Int, Int>>()
            toMoveStack.addLast(Pair(robotPosition.first, robotPosition.second))
            var currentPosition = Pair(robotPosition.first + rowChange, robotPosition.second + columnChange)
            while (map[currentPosition.first][currentPosition.second] == 'O') {
                toMoveStack.addLast(Pair(currentPosition.first, currentPosition.second))
                currentPosition = Pair(currentPosition.first + rowChange, currentPosition.second + columnChange)
            }

            if (map[currentPosition.first][currentPosition.second] != '#') {
                while (toMoveStack.isNotEmpty()) {
                    val (toMoveX, toMoveY) = toMoveStack.removeLast()
                    map[toMoveX + rowChange][toMoveY + columnChange] = map[toMoveX][toMoveY]
                    map[toMoveX][toMoveY] = '.'
                }

                return Pair(robotPosition.first + rowChange, robotPosition.second + columnChange)
            }

            return robotPosition
        }

        // for part 2
        private fun makeLeftOrRightMove(map: List<CharArray>, robotPosition: Pair<Int, Int>, columnChange: Int): Pair<Int, Int> {
            val toMoveStack = ArrayDeque<Pair<Int, Int>>()
            toMoveStack.addLast(Pair(robotPosition.first, robotPosition.second))
            var currentPosition = Pair(robotPosition.first, robotPosition.second + columnChange)
            while (map[currentPosition.first][currentPosition.second] == '['
                || map[currentPosition.first][currentPosition.second] == ']') {
                toMoveStack.addLast(Pair(currentPosition.first, currentPosition.second))
                currentPosition = Pair(currentPosition.first, currentPosition.second + columnChange)
            }

            if (map[currentPosition.first][currentPosition.second] != '#') {
                while (toMoveStack.isNotEmpty()) {
                    val (toMoveX, toMoveY) = toMoveStack.removeLast()
                    map[toMoveX][toMoveY + columnChange] = map[toMoveX][toMoveY]
                    map[toMoveX][toMoveY] = '.'
                }

                return Pair(robotPosition.first, robotPosition.second + columnChange)
            }

            return robotPosition
        }

        // for part 2
        private fun makeUpOrDownMove(map: List<CharArray>, robotPosition: Pair<Int, Int>, rowChange: Int): Pair<Int, Int> {
            val checkingQueue = ArrayDeque<Pair<Int, Int>>()
            checkingQueue.addFirst(Pair(robotPosition.first, robotPosition.second))
            val toMoveStack = ArrayDeque<Pair<Int, Int>>()
            val checkedBoxes = mutableSetOf<Pair<Int, Int>>()
            var encounteredWall = false
            while (checkingQueue.isNotEmpty() && !encounteredWall) {
                val (currentRow, currentColumn) = checkingQueue.removeLast()
                toMoveStack.addLast(Pair(currentRow, currentColumn))
                val currentSymbol = map[currentRow][currentColumn]
                if (map[currentRow + rowChange][currentColumn] == '#') {
                    encounteredWall = true
                }

                if (map[currentRow + rowChange][currentColumn] == '[' && (currentSymbol == '[' || currentSymbol == '@')
                    || map[currentRow + rowChange][currentColumn] == ']' && (currentSymbol == ']' || currentSymbol == '@')) {
                    val boxToCheck = Pair(currentRow + rowChange, currentColumn)
                    if (!checkedBoxes.contains(boxToCheck)) {
                        checkingQueue.addFirst(boxToCheck)
                        checkedBoxes.add(boxToCheck)
                    }
                    if (map[currentRow + rowChange][currentColumn] == '[') {
                        val restOfBoxToCheck = Pair(currentRow + rowChange, currentColumn + 1)
                        if (!checkedBoxes.contains(restOfBoxToCheck)) {
                            checkingQueue.addFirst(restOfBoxToCheck)
                            checkedBoxes.add(restOfBoxToCheck)
                        }
                    } else {
                        val restOfBoxToCheck = Pair(currentRow + rowChange, currentColumn - 1)
                        if (!checkedBoxes.contains(restOfBoxToCheck)) {
                            checkingQueue.addFirst(restOfBoxToCheck)
                            checkedBoxes.add(restOfBoxToCheck)
                        }
                    }
                }

                if (map[currentRow + rowChange][currentColumn] == '[' && currentSymbol == ']') {
                    val movedBoxLeft = Pair(currentRow + rowChange, currentColumn)
                    val movedBoxRight = Pair(currentRow + rowChange, currentColumn + 1)
                    if (!checkedBoxes.contains(movedBoxLeft) && !checkedBoxes.contains(movedBoxRight)) {
                        checkingQueue.addFirst(movedBoxLeft)
                        checkingQueue.addFirst(movedBoxRight)
                        checkedBoxes.add(movedBoxLeft)
                        checkedBoxes.add(movedBoxRight)
                    }
                }

                if (map[currentRow + rowChange][currentColumn] == ']' && currentSymbol == '[') {
                    val movedBoxLeft = Pair(currentRow + rowChange, currentColumn)
                    val movedBoxRight = Pair(currentRow + rowChange, currentColumn - 1)
                    if (!checkedBoxes.contains(movedBoxLeft) && !checkedBoxes.contains(movedBoxRight)) {
                        checkingQueue.addFirst(movedBoxLeft)
                        checkingQueue.addFirst(movedBoxRight)
                        checkedBoxes.add(movedBoxLeft)
                        checkedBoxes.add(movedBoxRight)
                    }
                }
            }

            if (!encounteredWall) {
                while (toMoveStack.isNotEmpty()) {
                    val (toMoveX, toMoveY) = toMoveStack.removeLast()
                    map[toMoveX + rowChange][toMoveY] = map[toMoveX][toMoveY]
                    map[toMoveX][toMoveY] = '.'
                }

                return Pair(robotPosition.first + rowChange, robotPosition.second)
            }

            return robotPosition
        }

        private fun findRobot(map: List<CharArray>): Pair<Int, Int> {
            for (i in map.indices) {
                for (j in map[i].indices) {
                    if (map[i][j] == '@') {
                        return Pair(i, j)
                    }
                }
            }

            return Pair(-1, -1)
        }

        // for part 1
        private fun calculateCoordinatesFinalSum(finalMap: List<CharArray>): Int {
            var sum = 0
            for (i in finalMap.indices) {
                for (j in finalMap[i].indices) {
                    if (finalMap[i][j] == 'O') {
                        sum += 100 * i + j
                    }
                }
            }

            return sum
        }

        // for part 2
        private fun calculateLargerCoordinatesFinalSum(finalMap: List<CharArray>): Int {
            var sum = 0
            for (i in finalMap.indices) {
                var j = 0
                while (j < finalMap[i].size) {
                    if (finalMap[i][j] == '[') {
                        sum += 100 * i + j
                        j += 2
                    } else {
                        j++
                    }
                }
            }

            return sum
        }

        // for part 2
        private fun prepareLargerMap(map: List<CharArray>): List<CharArray> {
            val newMap = List(map.size) { CharArray(2 * map[0].size) }
            for ((newMapX, i) in map.indices.withIndex()) {
                var newMapY = 0
                for (j in map[i].indices) {
                    when {
                        map[i][j] == '@' -> {
                            newMap[newMapX][newMapY] = '@'
                            newMap[newMapX][newMapY + 1] = '.'
                        }
                        map[i][j] == '#' -> {
                            newMap[newMapX][newMapY] = '#'
                            newMap[newMapX][newMapY + 1] = '#'
                        }
                        map[i][j] == '.' -> {
                            newMap[newMapX][newMapY] = '.'
                            newMap[newMapX][newMapY + 1] = '.'
                        }
                        map[i][j] == 'O' -> {
                            newMap[newMapX][newMapY] = '['
                            newMap[newMapX][newMapY + 1] = ']'
                        }
                    }

                    newMapY += 2
                }
            }

            return newMap
        }

        private fun parseInput(input: String): Pair<List<CharArray>, List<Char>> {
            val splitInput = input.split("\n")
            val indexOfBlankLine = splitInput.indexOf("")
            val mapArray = splitInput.subList(0, indexOfBlankLine)
                .map { it.toCharArray() }

            val moves = splitInput.subList(indexOfBlankLine + 1, splitInput.size)
                .flatMap { it.toCharArray().toList() }

            return Pair(mapArray, moves)
        }
    }
}