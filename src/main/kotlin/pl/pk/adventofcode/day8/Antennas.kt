package pl.pk.adventofcode.day8

import kotlin.math.abs

class Antennas {

    companion object {

        // for part 2
        fun countAntinodesInLine(input: String): Int {
            val map = parseInput(input)
            val rows = map.size
            val columns = map[0].size
            val frequenciesPositions = getFrequenciesPositions(map)
            val foundAntinodes = mutableSetOf<Pair<Int, Int>>()
            frequenciesPositions.forEach {
                for (i in 0..<it.value.size - 1) {
                    for (j in i + 1..<it.value.size) {
                        val firstPosition = it.value[i]
                        val secondPosition = it.value[j]
                        val difference =
                            Pair(firstPosition.first - secondPosition.first, firstPosition.second - secondPosition.second)
                        val gcd = gcd(abs(difference.first), abs(difference.second))
                        val dividedDifference = Pair(difference.first / gcd, difference.second / gcd)
                        var currentPosition = firstPosition
                        while (currentPosition.first in 0..<rows && currentPosition.second in 0..<columns) {
                            foundAntinodes.add(currentPosition)
                            currentPosition = Pair(currentPosition.first + dividedDifference.first,
                                currentPosition.second + dividedDifference.second)
                        }

                        currentPosition = firstPosition
                        while (currentPosition.first in 0..<rows && currentPosition.second in 0..<columns) {
                            foundAntinodes.add(currentPosition)
                            currentPosition = Pair(currentPosition.first - dividedDifference.first,
                                currentPosition.second - dividedDifference.second)
                        }
                    }
                }
            }

            return foundAntinodes.size
        }

        // for part 1
        fun countAntinodes(input: String): Int {
            val map = parseInput(input)
            val rows = map.size
            val columns = map[0].size
            val frequenciesPositions = getFrequenciesPositions(map)
            val foundAntinodes = mutableSetOf<Pair<Int, Int>>()
            frequenciesPositions.forEach {
                for (i in 0..<it.value.size - 1) {
                    for (j in i + 1..<it.value.size) {
                        val firstPosition = it.value[i]
                        val secondPosition = it.value[j]
                        val firstSecondDiff =
                            Pair(firstPosition.first - secondPosition.first, firstPosition.second - secondPosition.second)
                        val firstPossibleAntinode = Pair(firstPosition.first + firstSecondDiff.first,
                            firstPosition.second + firstSecondDiff.second)
                        if (firstPossibleAntinode.first in 0..<rows && firstPossibleAntinode.second in 0..<columns) {
                            foundAntinodes.add(firstPossibleAntinode)
                        }
                        if (firstSecondDiff.first % 2 == 0 && firstSecondDiff.second % 2 == 0) {
                            val possibleAntinode = Pair(firstPosition.first - firstSecondDiff.first / 2,
                                firstPosition.second - firstSecondDiff.second / 2)
                            if (possibleAntinode.first in 0..<rows && possibleAntinode.second in 0..<columns) {
                                foundAntinodes.add(possibleAntinode)
                            }
                        }

                        val secondFirstDiff =
                            Pair(secondPosition.first - firstPosition.first, secondPosition.second - firstPosition.second)
                        val secondPossibleAntinode = Pair(secondPosition.first + secondFirstDiff.first,
                            secondPosition.second + secondFirstDiff.second)
                        if (secondPossibleAntinode.first in 0..<rows && secondPossibleAntinode.second in 0..<columns) {
                            foundAntinodes.add(secondPossibleAntinode)
                        }
                        if (secondFirstDiff.first % 2 == 0 && secondFirstDiff.second % 2 == 0) {
                            val possibleAntinode = Pair(secondPosition.first - secondFirstDiff.first / 2,
                                secondPosition.second - secondFirstDiff.second / 2)
                            if (possibleAntinode.first in 0..<rows && possibleAntinode.second in 0..<columns) {
                                foundAntinodes.add(possibleAntinode)
                            }
                        }
                    }
                }
            }

            return foundAntinodes.size
        }

        private fun getFrequenciesPositions(map: List<CharArray>): Map<Char, List<Pair<Int, Int>>> {
            val frequenciesPositions = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
            for (i in map.indices) {
                for (j in map[i].indices) {
                    if (map[i][j] != '.') {
                        frequenciesPositions.getOrPut(map[i][j]) { mutableListOf() }.addLast(Pair(i, j))
                    }
                }
            }

            return frequenciesPositions
        }

        private fun gcd(a: Int, b: Int): Int {
            var num1 = a
            var num2 = b
            while (num2 != 0) {
                val temp = num2
                num2 = num1 % num2
                num1 = temp
            }
            return num1
        }

        private fun parseInput(input: String) =
            input.split("\n")
                .map { it.toCharArray() }
    }
}