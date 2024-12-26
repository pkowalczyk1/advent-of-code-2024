package pl.pk.adventofcode

import pl.pk.adventofcode.day25.Keys

fun main() {
    val input = """
#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####
    """.trimIndent()

    println(Keys.countFittingLockKeyPairs(input))
}