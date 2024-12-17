package pl.pk.adventofcode.day17

import kotlin.math.pow

class Program {

    companion object {

        fun printLowestAValueToOutputProgram(input: String) {
            val registers = parseRegisters(input)
            registers[0] = 0
            val program = parseProgram(input)
            getLowestAValueToOutputProgramFromIndex(registers, program, 0)
        }

        private fun getLowestAValueToOutputProgramFromIndex(registers: MutableList<Long>, program: List<Int>, index: Int) {
            val output = getProgramOutput(registers, program)
            if (output == program) {
                println(registers[0])
                return
            }

            if (index == 0 || output == program.subList(program.size - index, program.size)) {
                for (i in 0..8) {
                    val copiedRegisters = registers.toMutableList()
                    copiedRegisters[0] = 8 * copiedRegisters[0] + i
                    getLowestAValueToOutputProgramFromIndex(copiedRegisters, program, index + 1)
                }
            }
        }

        // for part 1
        fun getProgramOutput(registers: MutableList<Long>, program: List<Int>): List<Int> {
            val copiedRegisters = registers.toMutableList()
            val instructionPointer = InstructionPointer(0)
            val out = mutableListOf<Int>()
            while (instructionPointer.pointer < program.size) {
                val operand = program[instructionPointer.pointer + 1]
                when (program[instructionPointer.pointer]) {
                    0 -> copiedRegisters[0] = copiedRegisters[0] shr getComboOperand(operand, copiedRegisters).toInt()
                    1 -> copiedRegisters[1] = copiedRegisters[1] xor operand.toLong()
                    2 -> copiedRegisters[1] = getComboOperand(operand, copiedRegisters) % 8
                    3 -> if (copiedRegisters[0] != 0L) instructionPointer.pointer = operand - 2
                    4 -> copiedRegisters[1] = copiedRegisters[1] xor copiedRegisters[2]
                    5 -> out.add((getComboOperand(operand, copiedRegisters) % 8).toInt())
                    6 -> copiedRegisters[1] = copiedRegisters[0] shr getComboOperand(operand, copiedRegisters).toInt()
                    7 -> copiedRegisters[2] = copiedRegisters[0] shr getComboOperand(operand, copiedRegisters).toInt()
                    else -> {}
                }

                instructionPointer.pointer += 2
            }

            return out
        }

        private fun getComboOperand(operand: Int, registers: List<Long>): Long =
            when (operand) {
                0 -> 0
                1 -> 1
                2 -> 2
                3 -> 3
                4 -> registers[0]
                5 -> registers[1]
                6 -> registers[2]
                else -> -1
            }

        private fun parseRegisters(input: String): MutableList<Long> {
            val regexA = Regex("Register A: (\\d+)")
            val regexB = Regex("Register B: (\\d+)")
            val regexC = Regex("Register C: (\\d+)")

            val matchResultA = regexA.find(input)
            val registerA = matchResultA!!.groupValues[1].toLong()

            val matchResultB = regexB.find(input)
            val registerB = matchResultB!!.groupValues[1].toLong()

            val matchResultC = regexC.find(input)
            val registerC = matchResultC!!.groupValues[1].toLong()

            return mutableListOf(registerA, registerB, registerC)
        }

        private fun parseProgram(input: String): List<Int> {
            val regex = Regex("Program: ([0-9,]+)")
            val matchResult = regex.find(input)

            return matchResult!!.groupValues[1]
                .split(",")
                .map { it.toInt() }
        }
    }

    private data class InstructionPointer(var pointer: Int)
}