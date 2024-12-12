package pl.pk.adventofcode.day11

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class PlutoStones {

    companion object {

        fun getStonesCountAfterBlinks(stones: List<Long>, blinks: Int): Long {
            val stonesCountByNumber = mutableMapOf<Long, Long>()
            stones.forEach { stonesCountByNumber[it] = 1L }

            for (i in 1..blinks) {
                processBlink(stonesCountByNumber)
            }

            return stonesCountByNumber.values.sum()
        }

        private fun processBlink(stonesCountByNumber: MutableMap<Long, Long>) {
            val copy = stonesCountByNumber.toMutableMap()
            copy.forEach { (stone, count) ->
                if (count != 0L) {
                    if (stone == 0L) {
                        stonesCountByNumber[1] = stonesCountByNumber.getOrDefault(1, 0) + count
                        stonesCountByNumber[0] = stonesCountByNumber[0]!! - count
                    } else {
                        val digits = countDigits(stone)
                        stonesCountByNumber[stone] = stonesCountByNumber[stone]!! - count
                        if (digits % 2 == 0) {
                            val power = 10.0.pow(digits / 2).toLong()
                            val leftHalf = stone / power
                            val rightHalf = stone % power
                            stonesCountByNumber[leftHalf] = stonesCountByNumber.getOrDefault(leftHalf, 0) + count
                            stonesCountByNumber[rightHalf] = stonesCountByNumber.getOrDefault(rightHalf, 0) + count
                        } else {
                            val newStone = 2024 * stone
                            stonesCountByNumber[newStone] = stonesCountByNumber.getOrDefault(newStone, 0) + count
                        }
                    }
                }
            }
        }

        private fun countDigits(number: Long) = ((floor(log10(number.toDouble())) + 1)).toInt()
    }
}