package pl.pk.adventofcode.day5

class UpdatesPrinter {

    companion object {

        // for part 2
        fun correctPageListsAndSumIds(input: String): Int {
            val (rules, pageLists) = parseInput(input)
            val incorrect = pageLists.filter { !isListCorrect(it, rules) }
            incorrect.forEach {
                while (!isListCorrect(it, rules)) {
                    for (i in it.indices) {
                        for (j in i + 1..<it.size) {
                            if (rules.getOrDefault(it[j], mutableSetOf()).contains(it[i])) {
                                val tmp = it[i]
                                it[i] = it[j]
                                it[j] = tmp
                            }
                        }
                    }
                }
            }

            return incorrect.sumOf { it[it.size / 2] }
        }

        // for part 1
        fun getCorrectPageListsIdsSum(input: String): Int {
            val (rules, pageLists) = parseInput(input)
            return pageLists.filter { isListCorrect(it, rules) }
                .sumOf { it[it.size / 2] }
        }

        private fun isListCorrect(pageList: List<Int>, rules: Map<Int, Set<Int>>): Boolean {
            val encounteredNumbers = mutableSetOf<Int>()
            pageList.forEach {
                if (rules.containsKey(it)) {
                    if (rules[it]!!.any { number -> encounteredNumbers.contains(number) }) {
                        return false
                    }
                }

                encounteredNumbers.add(it)
            }

            return true
        }

        private fun parseInput(input: String): Pair<Map<Int, Set<Int>>, List<MutableList<Int>>> {
            val splitInput = input.split("\n\n")
            val rules = mutableMapOf<Int, MutableSet<Int>>()
            splitInput[0].split("\n")
                .forEach {
                    val rule = it.split("|")
                        .map(String::toInt)
                    val setForRule = rules.getOrPut(rule[0]) { mutableSetOf() }
                    setForRule.add(rule[1])
                }

            val pageLists = splitInput[1].split("\n")
                .map { it.split(",").map(String::toInt).toMutableList() }

            return Pair(rules, pageLists)
        }
    }
}