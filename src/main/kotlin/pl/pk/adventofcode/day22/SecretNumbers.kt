package pl.pk.adventofcode.day22

class SecretNumbers {

    companion object {

        // for part 2
        fun getMostBananas(input: String): Int {
            val initialNumbers = parseInput(input)
            val bananasByDifferenceSequence = mutableMapOf<String, Int>()
            for (number in initialNumbers) {
                updateBananasByDifferenceSequenceForInitialNumber(number, bananasByDifferenceSequence)
            }

            return bananasByDifferenceSequence.values.max()
        }

        // for part 2
        private fun updateBananasByDifferenceSequenceForInitialNumber(initialNumber: Long,
                                                                      bananasBySequence: MutableMap<String, Int>) {
            val sequenceUpdated = mutableSetOf<String>()
            var firstDifference: Int?
            var secondDifference: Int? = null
            var thirdDifference: Int? = null
            var fourthDifference: Int? = null
            var previous: Long
            var current = initialNumber
            for (i in 0..<2000) {
                previous = current
                current = getNextSecretNumber(current)
                val difference = (current % 10).toInt() - (previous % 10).toInt()
                firstDifference = secondDifference
                secondDifference = thirdDifference
                thirdDifference = fourthDifference
                fourthDifference = difference

                if (firstDifference != null && secondDifference != null && thirdDifference != null) {
                    val sequence = firstDifference.toString() + secondDifference.toString() + thirdDifference.toString() +
                        fourthDifference.toString()
                    if (!sequenceUpdated.contains(sequence)) {
                        sequenceUpdated.add(sequence)
                        bananasBySequence.compute(sequence) { _, bananas -> (bananas ?: 0) + (current % 10).toInt() }
                    }
                }
            }
        }

        // for part 1
        fun getSumOf2000thSecretNumbers(input: String): Long {
            val initialNumbers = parseInput(input)
            return initialNumbers.sumOf {
                var current = it
                for (i in 0..<2000) {
                    current = getNextSecretNumber(current)
                }
                current
            }
        }

        private fun getNextSecretNumber(current: Long): Long {
            var result = ((current * 64) xor current) % 16777216
            result = ((result / 32) xor result) % 16777216
            result = ((result * 2048) xor result) % 16777216
            return result
        }

        private fun parseInput(input: String): List<Long> = input.split("\n").map { it.toLong() }.toList()
    }
}