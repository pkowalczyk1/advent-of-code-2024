package pl.pk.adventofcode.day1

import java.util.PriorityQueue
import kotlin.math.abs

class Locations {

    companion object {

        // for part 2
        fun getListsSimilarity(input: String): Int {
            val (leftList, rightList) = parseInput(input)
            val rightListCountByNumber = mutableMapOf<Int, Int>()
            rightList.forEach { rightListCountByNumber.compute(it) { _, count -> (count ?: 0) + 1 } }

            return leftList.sumOf { it * rightListCountByNumber.getOrDefault(it, 0) }
        }

        // for part 1
        fun getListsDistance(input: String): Int {
            val (leftList, rightList) = parseInput(input)
            val leftHeap = PriorityQueue<Int>(compareByDescending { it })
            val rightHeap = PriorityQueue<Int>(compareByDescending { it })
            leftList.forEach { leftHeap.add(it) }
            rightList.forEach { rightHeap.add(it) }

            var distance = 0
            while (leftHeap.isNotEmpty() && rightHeap.isNotEmpty()) {
                val leftValue = leftHeap.poll()
                val rightValue = rightHeap.poll()
                distance += abs(leftValue - rightValue)
            }

            return distance
        }

        private fun parseInput(input: String): Pair<List<Int>, List<Int>> {
            val leftList = mutableListOf<Int>()
            val rightList = mutableListOf<Int>()
            input.lines()
                .map { it.split("""\s+""".toRegex()).map(String::toInt) }
                .forEach {
                    leftList.addLast(it[0])
                    rightList.addLast(it[1])
                }

            return Pair(leftList, rightList)
        }
    }
}