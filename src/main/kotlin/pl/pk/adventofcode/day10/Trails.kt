package pl.pk.adventofcode.day10

class Trails {

    companion object {

        // for part 2
        fun sumTrailheadsRatings(input: String): Int {
            val map = parseInput(input)
            val rows = map.size
            val columns = map[0].size
            val trailheads = mutableSetOf<Pair<Int, Int>>()
            for (i in map.indices) {
                for (j in map[i].indices) {
                    if (map[i][j] == 0) {
                        trailheads.add(Pair(i, j))
                    }
                }
            }

            val ratingByTrailhead = mutableMapOf<Pair<Int, Int>, Int>()
            for (trailhead in trailheads) {
                val fieldQueue = ArrayDeque<Pair<Int, Int>>()
                fieldQueue.addFirst(trailhead)
                while (fieldQueue.isNotEmpty()) {
                    val currentField = fieldQueue.removeLast()
                    val value = map[currentField.first][currentField.second]
                    if (value == 9) {
                        ratingByTrailhead.compute(trailhead) { _, score -> (score ?: 0) + 1 }
                    } else {
                        if (isCorrectIndex(currentField.first + 1, rows)
                            && map[currentField.first + 1][currentField.second] == value + 1) {
                            fieldQueue.addFirst(Pair(currentField.first + 1, currentField.second))
                        }

                        if (isCorrectIndex(currentField.first - 1, rows)
                            && map[currentField.first - 1][currentField.second] == value + 1) {
                            fieldQueue.addFirst(Pair(currentField.first - 1, currentField.second))
                        }

                        if (isCorrectIndex(currentField.second + 1, columns)
                            && map[currentField.first][currentField.second + 1] == value + 1) {
                            fieldQueue.addFirst(Pair(currentField.first, currentField.second + 1))
                        }

                        if (isCorrectIndex(currentField.second - 1, columns)
                            && map[currentField.first][currentField.second - 1] == value + 1) {
                            fieldQueue.addFirst(Pair(currentField.first, currentField.second - 1))
                        }
                    }
                }
            }

            return ratingByTrailhead.entries.sumOf { it.value }
        }

        // for part 1
        fun sumTrailheadsScores(input: String): Int {
            val map = parseInput(input)
            val rows = map.size
            val columns = map[0].size
            val trailheads = mutableSetOf<Pair<Int, Int>>()
            for (i in map.indices) {
                for (j in map[i].indices) {
                    if (map[i][j] == 0) {
                        trailheads.add(Pair(i, j))
                    }
                }
            }

            val scoreByTrailhead = mutableMapOf<Pair<Int, Int>, Int>()
            for (trailhead in trailheads) {
                val visitedFields = mutableSetOf<Pair<Int, Int>>()
                val fieldQueue = ArrayDeque<Pair<Int, Int>>()
                fieldQueue.addFirst(trailhead)
                while (fieldQueue.isNotEmpty()) {
                    val currentField = fieldQueue.removeLast()
                    if (!visitedFields.contains(currentField)) {
                        val value = map[currentField.first][currentField.second]
                        if (value == 9) {
                            scoreByTrailhead.compute(trailhead) { _, score -> (score ?: 0) + 1 }
                            visitedFields.add(currentField)
                        } else {
                            if (isCorrectIndex(currentField.first + 1, rows)
                                && map[currentField.first + 1][currentField.second] == value + 1) {
                                fieldQueue.addFirst(Pair(currentField.first + 1, currentField.second))
                            }

                            if (isCorrectIndex(currentField.first - 1, rows)
                                && map[currentField.first - 1][currentField.second] == value + 1) {
                                fieldQueue.addFirst(Pair(currentField.first - 1, currentField.second))
                            }

                            if (isCorrectIndex(currentField.second + 1, columns)
                                && map[currentField.first][currentField.second + 1] == value + 1) {
                                fieldQueue.addFirst(Pair(currentField.first, currentField.second + 1))
                            }

                            if (isCorrectIndex(currentField.second - 1, columns)
                                && map[currentField.first][currentField.second - 1] == value + 1) {
                                fieldQueue.addFirst(Pair(currentField.first, currentField.second - 1))
                            }

                            visitedFields.add(currentField)
                        }
                    }
                }
            }

            return scoreByTrailhead.entries.sumOf { it.value }
        }

        private fun isCorrectIndex(index: Int, size: Int) = index in 0..<size

        private fun parseInput(input: String) =
            input.lines()
                .map { it.toCharArray() }
                .map { it.map { digit -> digit.toString().toInt() } }
    }
}