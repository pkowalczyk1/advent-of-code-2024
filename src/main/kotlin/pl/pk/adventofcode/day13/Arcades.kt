package pl.pk.adventofcode.day13

class Arcades {

    companion object {

        private const val A_BUTTON_COST = 3
        private const val B_BUTTON_COST = 1

        fun getFewestTokensToWinAll(input: String): Long {
            val machines = parseInput(input)
            return machines.map { getFewestTokensForMachine(it) }
                .filter { it != -1L }
                .reduce { a, b -> a + b }
        }

        private fun getFewestTokensForMachine(machine: MachineConf): Long {
            val bButtonUsages = getBButtonUsages(machine)
            val aButtonUsages = getAButtonUsages(machine, bButtonUsages)
            if (aButtonUsages == -1L || bButtonUsages == -1L) {
                return -1
            }

            return aButtonUsages * A_BUTTON_COST + bButtonUsages * B_BUTTON_COST
        }

        private fun getAButtonUsages(machine: MachineConf, bButtonUsages: Long): Long {
            if (bButtonUsages == -1L) {
                return -1
            }

            val numerator = machine.prizeX - bButtonUsages * machine.bButtonX
            val denominator = machine.aButtonX

            if (numerator % denominator != 0L || denominator == 0) {
                return -1
            }

            val aButtonUsages = numerator / denominator
            if (aButtonUsages < 0) {
                return -1
            }

            return aButtonUsages
        }

        private fun getBButtonUsages(machine: MachineConf): Long {
            val numerator = machine.prizeY * machine.aButtonX - machine.prizeX * machine.aButtonY
            val denominator = machine.bButtonY * machine.aButtonX - machine.bButtonX * machine.aButtonY
            if (numerator % denominator != 0L || denominator == 0) {
                return -1
            }

            val bButtonUsages = numerator / denominator
            if (bButtonUsages < 0) {
                return -1
            }

            return bButtonUsages
        }

        private fun parseInput(input: String): List<MachineConf> {
            val buttonValuesRegex = """\+(\d+)""".toRegex()
            val prizePositionRegex = """=(\d+)""".toRegex()
            val inputLines = input.split("\n")
            val machines = mutableListOf<MachineConf>()
            for (index in 0..inputLines.lastIndex step 4) {
                val aButtonLine = inputLines[index]
                val bButtonLine = inputLines[index + 1]
                val prizeLine = inputLines[index + 2]
                val aButtonValues = buttonValuesRegex.findAll(aButtonLine)
                    .map { it.groupValues[1].toInt() }
                    .toList()
                val bButtonValues = buttonValuesRegex.findAll(bButtonLine)
                    .map { it.groupValues[1].toInt() }
                    .toList()
                val prizePositions = prizePositionRegex.findAll(prizeLine)
                    .map { it.groupValues[1].toLong() + 10000000000000 }
                    .toList()
                val machine = MachineConf(prizePositions[0], prizePositions[1], aButtonValues[0], aButtonValues[1],
                    bButtonValues[0], bButtonValues[1])
                machines.addLast(machine)
            }

            return machines
        }
    }

    data class MachineConf(val prizeX: Long,
                           val prizeY: Long,
                           val aButtonX: Int,
                           val aButtonY: Int,
                           val bButtonX: Int,
                           val bButtonY: Int)
}