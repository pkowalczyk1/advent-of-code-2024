package pl.pk.adventofcode.day24

import pl.pk.adventofcode.day24.BitWires.Operation.AND
import pl.pk.adventofcode.day24.BitWires.Operation.OR
import pl.pk.adventofcode.day24.BitWires.Operation.XOR
import java.util.function.BiFunction

class BitWires {

    companion object {

        // for part 2
        fun getSwappedOutputs(input: String): String {
            val operations = parseOperations(input)
            val mostSignificantBit = operations.map { it.result }.maxOf { it }
            val swappedOutputs = mutableSetOf<String>()
            operations.forEach {
                if (it.operation != XOR && it.result[0] == 'z' && it.result != mostSignificantBit) {
                    swappedOutputs.add(it.result)
                }

                if (it.operation == XOR && it.leftOperand[0] != 'x' && it.leftOperand[0] != 'y' && it.rightOperand[0] != 'x'
                    && it.rightOperand[0] != 'y' && it.result[0] != 'z') {
                    swappedOutputs.add(it.result)
                }

                if (it.operation == AND && it.leftOperand != "x00" && it.rightOperand != "x00") {
                    for (subOperation in operations) {
                        if ((it.result == subOperation.leftOperand || it.result == subOperation.rightOperand)
                            && subOperation.operation != OR) {
                            swappedOutputs.add(it.result)
                        }
                    }
                }

                if (it.operation == XOR) {
                    for (subOperation in operations) {
                        if ((it.result == subOperation.leftOperand || it.result == subOperation.rightOperand)
                            && subOperation.operation == OR) {
                            swappedOutputs.add(it.result)
                        }
                    }
                }
            }

            return swappedOutputs.sorted().joinToString(",")
        }

        // for part 1
        fun getOperationsResult(input: String): Long {
            val values = parseValues(input)
            val operations = parseOperations(input)
            val doneOperations = mutableSetOf<OperandsWithOperationAndResult>()
            while (doneOperations.size != operations.size) {
                operations.forEach {
                    if (values.containsKey(it.leftOperand) && values.containsKey(it.rightOperand)) {
                        val result = it.operation.function.apply(values[it.leftOperand]!!, values[it.rightOperand]!!)
                        values[it.result] = result
                        doneOperations.add(it)
                    }
                }
            }

            return values.filter { it.key.startsWith("z") }
                .entries.toList()
                .sortedByDescending { it.key }
                .joinToString("") { it.value.toString() }
                .toLong(2)
        }

        private fun parseValues(input: String): MutableMap<String, Int>{
            val lines = input.lines()
            val blankLineIndex = lines.indexOf("")
            val regex = """(\w+):\s*(\d+)""".toRegex()
            return lines.subList(0, blankLineIndex)
                .map {
                    val matchResult = regex.find(it)!!
                    Pair(matchResult.groupValues[1], matchResult.groupValues[2].toInt())
                }
                .associate { it }
                .toMutableMap()
        }

        private fun parseOperations(input: String): List<OperandsWithOperationAndResult> {
            val lines = input.lines()
            val blankLineIndex = lines.indexOf("")
            val regex = """(\w+)\s+(\w{2,3})\s+(\w+)\s+->\s+(\w+)""".toRegex()
            return lines.subList(blankLineIndex + 1, lines.size)
                .map {
                    val matchResult = regex.find(it)!!.groupValues
                    OperandsWithOperationAndResult(matchResult[1], matchResult[3], Operation.valueOf(matchResult[2]),
                        matchResult[4])
                }
        }
    }

    private data class OperandsWithOperationAndResult(val leftOperand: String,
                                                      val rightOperand: String,
                                                      val operation: Operation,
                                                      val result: String)

    private enum class Operation(val function: BiFunction<Int, Int, Int>) {
        AND({ a, b -> a and b }),
        OR({ a, b -> a or b }),
        XOR({ a, b -> a xor b })
    }
}