package pl.pk.adventofcode.day19

class ColorPatterns {

    companion object {

        // for part 2
        fun countAllPossibleWaysToGetPatterns(input: String): Long {
            val atomicPatterns = parseAtomicPatternsList(input)
            val patterns = parsePatterns(input)
            return patterns.sumOf { getWaysToGetPatternCount(it, atomicPatterns) }
        }

        // for part 2, recursion with memoization
        private fun getWaysToGetPatternCount(pattern: String, atomicPatterns: List<String>,
                                             waysCountByPattern: MutableMap<String, Long> = mutableMapOf()): Long {
            if (pattern.isEmpty()) {
                return 1
            }

            return waysCountByPattern.getOrPut(pattern) {
                atomicPatterns.filter { pattern.startsWith(it) }
                    .sumOf { getWaysToGetPatternCount(pattern.removePrefix(it), atomicPatterns, waysCountByPattern) }
            }
        }

        // for part 1
        fun countPossiblePatterns(input: String): Int {
            val atomicPatterns = parseAtomicPatternsList(input)
            val patterns = parsePatterns(input)
            val patternPossibilityByPattern = mutableMapOf<String, Boolean>()
            var count = 0
            patterns.forEach {
                if (isPatternPossible(it, atomicPatterns, patternPossibilityByPattern)) {
                    count++
                }
            }

            return count
        }

        // for part 1, dynamic programming
        private fun isPatternPossible(pattern: String, atomicPatterns: List<String>,
                              patternPossibilityByPattern: MutableMap<String, Boolean>): Boolean {
            atomicPatterns.forEach { patternPossibilityByPattern[it] = true }
            for (length in 1..pattern.length) {
                for (start in 0..pattern.length - length) {
                    val patternPart = pattern.substring(start, start + length)
                    for (i in 1..<patternPart.length) {
                        if (patternPossibilityByPattern.getOrDefault(patternPart.substring(0, i), false)
                            && patternPossibilityByPattern.getOrDefault(patternPart.substring(i, patternPart.length), false)) {
                            patternPossibilityByPattern[patternPart] = true
                            break
                        }
                    }
                }
            }

            return patternPossibilityByPattern.getOrDefault(pattern, false)
        }

        private fun parseAtomicPatternsList(input: String) =
            input.split("\n")
                .first()
                .split(", ")
                .toList()

        private fun parsePatterns(input: String) =
            input.split("\n")
                .drop(2)
    }
}