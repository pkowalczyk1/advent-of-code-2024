package pl.pk.adventofcode.day3

class Multiplications {

    companion object {

        // for part 2
        fun getMultiplicationsResult(input: String): Int {
            val regexForValues = """mul\((\d+),(\d+)\)""".toRegex()
            val regexForDo = """do\(\)""".toRegex()
            val regexForDont = """don't\(\)""".toRegex()
            val doMatches = regexForDo.findAll(input).toList()
            val dontMatches = regexForDont.findAll(input).toList()
            var sum = 0
            var startIndex = 0
            while (startIndex < input.length) {
                val dontLocation = dontMatches.firstOrNull { it.range.first > startIndex }?.range
                    ?: IntRange(input.length, input.length)
                regexForValues.findAll(input.substring(startIndex, dontLocation.first)).forEach {
                    sum += it.groupValues[1].toInt() * it.groupValues[2].toInt()
                }

                val nextStart = doMatches.firstOrNull { it.range.first > dontLocation.last }?.range?.first
                if (nextStart == null) {
                    return sum
                } else {
                    startIndex = nextStart
                }
            }

            return sum
        }
    }
}