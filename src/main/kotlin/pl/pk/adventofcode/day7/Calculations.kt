package pl.pk.adventofcode.day7

import pl.pk.adventofcode.day7.Calculations.Operation.ADD
import pl.pk.adventofcode.day7.Calculations.Operation.CAT
import pl.pk.adventofcode.day7.Calculations.Operation.MUL
import java.util.function.BiFunction

class Calculations {

    companion object {

        fun getAchievableNumbersSum(input: String) =
            parseInput(input).sumOf {
                if (isAchievable(it.first, it.second, listOf(ADD, MUL, CAT))) {  // for part 1 don't pass CAT as operations
                    return@sumOf it.first
                }

                return@sumOf 0
            }

        private fun isAchievable(targetNumber: Long, numbers: List<Long>, operations: List<Operation>): Boolean {
            if (numbers.size == 1) {
                return numbers[0] == targetNumber
            }

            val firstNumber = numbers[0]
            val secondNumber = numbers[1]
            val numbersCopy = numbers.toMutableList()
            numbersCopy.removeFirst()
            for (operation in operations) {
                numbersCopy[0] = operation.function.apply(firstNumber, secondNumber)
                if (isAchievable(targetNumber, numbersCopy, operations)) {
                    return true
                }
            }

            return false
        }

        private fun parseInput(input: String): List<Pair<Long, List<Long>>> {
            val regex = """^(\d+):\s*(\d+(?:\s+\d+)*)$""".toRegex()
            return input.lines()
                .map {
                    val matchResult = regex.find(it)!!.groupValues
                    val targetNumber = matchResult[1].toLong()
                    val restOfNumbers = matchResult[2].split(" ").map(String::toLong)
                    Pair(targetNumber, restOfNumbers)
                }
        }
    }

    private enum class Operation(val function: BiFunction<Long, Long, Long>) {
        ADD({ a, b -> a + b }),
        MUL({ a, b -> a * b }),
        CAT({ a, b -> (a.toString() + b.toString()).toLong() })
    }
}