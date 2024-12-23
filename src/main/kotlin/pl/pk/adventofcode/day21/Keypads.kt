package pl.pk.adventofcode.day21

class Keypads {

    companion object {

        private val NUM_PAD = mapOf(
            Pair('7', Pair(0, 0)), Pair('8', Pair(0, 1)), Pair('9', Pair(0, 2)),
            Pair('4', Pair(1, 0)), Pair('5', Pair(1, 1)), Pair('6', Pair(1, 2)),
            Pair('1', Pair(1, 0)), Pair('2', Pair(2, 1)), Pair('3', Pair(2, 2)),
            Pair(' ', Pair(3, 0)), Pair('0', Pair(3, 1)), Pair('A', Pair(3, 2))
        )

        private val DIR_PAD = mapOf(
            Pair(' ', Pair(0, 0)), Pair('^', Pair(0, 1)), Pair('A', Pair(0, 2)),
            Pair('<', Pair(1, 0)), Pair('v', Pair(1, 1)), Pair('>', Pair(1, 2))
        )

        fun getCodesComplexity(codes: List<String>, depth: Int): Long {
            var result: Long = 0
            for (code in codes) {
                var steps = getStepsToPressButtons(code, PadToUse.NUM_PAD)
                for (i in 0..depth) {
                    steps = steps.map { getStepsToPressButtons(it.key + 'A', PadToUse.DIR_PAD, it.value) }
                        .flatMap { it.entries }
                        .groupBy({ it.key }, { it.value })
                        .mapValues { (_, values) -> values.sum() }
                }

                result += steps.values.sum() * code.substring(0, 3).toInt()
            }

            return result
        }

        private fun getStepsToPressButtons(buttons: String, padToUse: PadToUse, count: Long = 1): Map<String, Long> {
            val countBySequence = mutableMapOf<String, Long>()
            val pad = when (padToUse) {
                PadToUse.NUM_PAD -> NUM_PAD
                PadToUse.DIR_PAD -> DIR_PAD
            }

            val (forbiddenRow, forbiddenColumn) = pad[' ']!!
            var (prevRow, prevColumn) = pad['A']!!
            for (button in buttons) {
                val (currRow, currColumn) = pad[button]!!
                val invertResult = currRow == forbiddenRow && prevColumn == forbiddenColumn
                    || currColumn == forbiddenColumn && prevRow == forbiddenRow
                val result = StringBuilder()
                val rowDiff = currRow - prevRow
                val columnDiff = currColumn - prevColumn
                if (columnDiff < 0) {
                    for (i in 0..<-columnDiff) result.append("<")
                }
                if (rowDiff > 0) {
                    for (i in 0..<rowDiff) result.append("v")
                }
                if (rowDiff < 0) {
                    for (i in 0..<-rowDiff) result.append("^")
                }
                if (columnDiff > 0) {
                    for (i in 0..<columnDiff) result.append(">")
                }

                if (invertResult) {
                    countBySequence.compute(result.reversed().toString()) { _, value -> (value ?: 0) + count }
                } else {
                    countBySequence.compute(result.toString()) { _, value -> (value ?: 0) + count }
                }

                prevRow = currRow
                prevColumn = currColumn
            }

            return countBySequence
        }
    }

    private enum class PadToUse {
        NUM_PAD,
        DIR_PAD
    }
}