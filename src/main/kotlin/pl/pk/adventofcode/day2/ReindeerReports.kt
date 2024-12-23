package pl.pk.adventofcode.day2

import kotlin.math.abs
import kotlin.math.sign

class ReindeerReports {

    companion object {

        fun countSafeReports(input: String) = parseInput(input).count { isReportSafeWithDamping(it) }

        // for part 2
        private fun isReportSafeWithDamping(report: List<Int>): Boolean {
            val reportCopy = report.toMutableList()

            // checking if is safe without first value
            if (isReportSafe(report.subList(1, report.size))) {
                return true
            }

            // checking if is safe without second value
            val removed = reportCopy.removeAt(1)
            if (isReportSafe(reportCopy)) {
                return true
            }

            reportCopy.add(1, removed)

            val expectedDifferenceSign = (report[1] - report[0]).sign
            for (i in 0..<report.size - 1) {
                val firstNumber = report[i]
                val secondNumber = report[i + 1]
                val difference = secondNumber - firstNumber
                if (difference.sign != expectedDifferenceSign || abs(difference) < 1 || abs(difference) > 3) {
                    val removedFirst = reportCopy.removeAt(i)
                    if (isReportSafe(reportCopy)) {
                        return true
                    }

                    reportCopy.add(i, removedFirst)
                    reportCopy.removeAt(i + 1)
                    return isReportSafe(reportCopy)
                }
            }

            return true
        }

        // for part 1
        private fun isReportSafe(report: List<Int>): Boolean {
            val expectedDifferenceSign = (report[1] - report[0]).sign
            for (i in 0..<report.size - 1) {
                val firstNumber = report[i]
                val secondNumber = report[i + 1]
                val difference = secondNumber - firstNumber
                if (difference.sign != expectedDifferenceSign || abs(difference) < 1 || abs(difference) > 3) {
                    return false
                }
            }

            return true
        }

        private fun parseInput(input: String) = input.lines().map { it.split(" ").map(String::toInt) }
    }
}