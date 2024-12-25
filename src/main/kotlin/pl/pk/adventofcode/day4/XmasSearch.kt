package pl.pk.adventofcode.day4

class XmasSearch {

    companion object {

        // for part 2
        fun getMasInXCount(input: String): Int {
            val map = parseInput(input)
            var xmasCount = 0
            for (i in 0..map.size - 3) {
                for (j in 0..map[i].size - 3) {
                    val leftToRight = charArrayOf(map[i][j], map[i + 1][j + 1], map[i + 2][j + 2]).joinToString("")
                    val rightToLeft = charArrayOf(map[i][j + 2], map[i + 1][j + 1], map[i + 2][j]).joinToString("")
                    if ((leftToRight == "MAS" || leftToRight == "SAM") && (rightToLeft == "MAS" || rightToLeft == "SAM")) {
                        xmasCount++
                    }
                }
            }

            return xmasCount
        }

        // for part 1
        fun getXmasCount(input: String): Int {
            val map = parseInput(input)
            val rows = map.size
            val columns = map[0].size
            var xmasCount = 0
            for (i in map.indices) {
                for (j in map[i].indices) {
                    if (map[i][j] == 'X') {
                        if (isIndexCorrect(i + 3, rows)) {
                            if (String(charArrayOf(map[i][j], map[i + 1][j], map[i + 2][j], map[i + 3][j])) == "XMAS") {
                                xmasCount++
                            }
                        }

                        if (isIndexCorrect(j + 3, columns)) {
                            if (String(charArrayOf(map[i][j], map[i][j + 1], map[i][j + 2], map[i][j + 3])) == "XMAS") {
                                xmasCount++
                            }
                        }

                        if (isIndexCorrect(i - 3, rows)) {
                            if (String(charArrayOf(map[i][j], map[i - 1][j], map[i - 2][j], map[i - 3][j])) == "XMAS") {
                                xmasCount++
                            }
                        }

                        if (isIndexCorrect(j - 3, columns)) {
                            if (String(charArrayOf(map[i][j], map[i][j - 1], map[i][j - 2], map[i][j - 3])) == "XMAS") {
                                xmasCount++
                            }
                        }

                        if (isIndexCorrect(i + 3, rows) && isIndexCorrect(j + 3, columns)) {
                            if (String(charArrayOf(map[i][j], map[i + 1][j + 1], map[i + 2][j + 2], map[i + 3][j + 3]))
                                == "XMAS") {
                                xmasCount++
                            }
                        }

                        if (isIndexCorrect(i + 3, rows) && isIndexCorrect(j - 3, columns)) {
                            if (String(charArrayOf(map[i][j], map[i + 1][j - 1], map[i + 2][j - 2], map[i + 3][j - 3]))
                                == "XMAS") {
                                xmasCount++
                            }
                        }

                        if (isIndexCorrect(i - 3, rows) && isIndexCorrect(j + 3, columns)) {
                            if (String(charArrayOf(map[i][j], map[i - 1][j + 1], map[i - 2][j + 2], map[i - 3][j + 3]))
                                == "XMAS") {
                                xmasCount++
                            }
                        }

                        if (isIndexCorrect(i - 3, rows) && isIndexCorrect(j - 3, columns)) {
                            if (String(charArrayOf(map[i][j], map[i - 1][j - 1], map[i - 2][j - 2], map[i - 3][j - 3]))
                                == "XMAS") {
                                xmasCount++
                            }
                        }
                    }
                }
            }

            return xmasCount
        }

        private fun isIndexCorrect(index: Int, size: Int) = index in 0..<size

        private fun parseInput(input: String): List<CharArray> = input.lines().map { it.toCharArray() }
    }
}