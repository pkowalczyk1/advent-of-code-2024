package pl.pk.adventofcode.day25

class Keys {

    companion object {

        fun countFittingLockKeyPairs(input: String): Int {
            val locksAndKeys = parseInput(input)
            val locks = locksAndKeys.filter { it[0].contentEquals(charArrayOf('#', '#', '#', '#', '#')) }
                .toList()
                .map {
                    val columns = it[0].size
                    val heights = IntArray(columns)
                    for (j in 0..<columns) {
                        var i = 1
                        while (it[i][j] == '#') {
                            heights[j]++
                            i++
                        }
                    }

                    heights
                }
            val keys = locksAndKeys.filter { it[0].contentEquals(charArrayOf('.', '.', '.', '.', '.')) }
                .toList()
                .map {
                    val columns = it[0].size
                    val freeSpaces = IntArray(columns)
                    for (j in 0..<columns) {
                        var i = 1
                        while (it[i][j] == '.') {
                            freeSpaces[j]++
                            i++
                        }
                    }

                    freeSpaces
                }

            return keys.sumOf { key ->
                locks.count { lock -> isFitting(key, lock) }
            }
        }

        private fun isFitting(key: IntArray, lock: IntArray): Boolean {
            for (i in key.indices) {
                if (lock[i] > key[i]) {
                    return false
                }
            }

            return true
        }

        private fun parseInput(input: String) =
            input.trim()
                .split("\n\n")
                .map { block ->
                    block.split("\n")
                        .map { it.toCharArray() }
                }
    }
}