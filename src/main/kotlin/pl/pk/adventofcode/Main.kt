package pl.pk.adventofcode

import pl.pk.adventofcode.day4.XmasSearch

fun main() {
    val input = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent()

    println(XmasSearch.getMasInXCount(input))
}