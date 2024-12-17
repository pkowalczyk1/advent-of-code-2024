package pl.pk.adventofcode.day14

class Robots {

    companion object {

        private const val MAP_WIDTH = 101
        private const val MAP_HEIGHT = 103

        fun getSafetyFactor(input: String): Int {
            val robots = parseInput(input)
            for (seconds in 1..10000) {
                val finalRobotsCountByPosition = mutableMapOf<Pair<Int, Int>, Int>()
                for (robot in robots) {
                    var finalX = (robot.startPosX + robot.velocityX * seconds) % MAP_WIDTH
                    var finalY = (robot.startPosY + robot.velocityY * seconds) % MAP_HEIGHT
                    if (finalX < 0) {
                        finalX += MAP_WIDTH
                    }

                    if (finalY < 0) {
                        finalY += MAP_HEIGHT
                    }

                    val finalPosition = Pair(finalX, finalY)
                    finalRobotsCountByPosition.compute(finalPosition) { _, count -> (count ?: 0) + 1 }
                }

                if (isComplete5x5SquarePresent(finalRobotsCountByPosition)) {
                    println(seconds)
                    println("---------------------")
                    printMap(finalRobotsCountByPosition)
                }
            }

            // for part 1
//            val quadrantsBorderX = MAP_WIDTH / 2
//            val quadrantsBorderY = MAP_HEIGHT / 2
//            var leftUpperQuadrantCount = 0
//            var rightUpperQuadrantCount = 0
//            var leftLowerQuadrantCount = 0
//            var rightLowerQuadrantCount = 0
//            finalRobotsCountByPosition.forEach { (position, count) ->
//                if (position.first < quadrantsBorderX && position.second < quadrantsBorderY) {
//                    leftUpperQuadrantCount += count
//                } else if (position.first < quadrantsBorderX && position.second > quadrantsBorderY) {
//                    rightUpperQuadrantCount += count
//                } else if (position.first > quadrantsBorderX && position.second < quadrantsBorderY) {
//                    leftLowerQuadrantCount += count
//                } else if (position.first > quadrantsBorderX && position.second > quadrantsBorderY) {
//                    rightLowerQuadrantCount += count
//                }
//            }
//
//            return leftUpperQuadrantCount * rightUpperQuadrantCount * leftLowerQuadrantCount * rightLowerQuadrantCount
            return 0
        }

        private fun isComplete5x5SquarePresent(finalRobotsCountByPosition: Map<Pair<Int, Int>, Int>): Boolean {
            for (j in 0..MAP_HEIGHT - 5) {
                for (i in 0..MAP_WIDTH - 5) {
                    var skip = false
                    for (k in 0..5) {
                        for (l in 0..5) {
                            if (!finalRobotsCountByPosition.containsKey(Pair(i + k, j + l))) {
                                skip = true
                                break
                            }
                        }

                        if (skip) {
                            break
                        }
                    }

                    if (!skip) {
                        return true
                    }
                }
            }

            return false
        }

        private fun printMap(finalRobotsCountByPosition: Map<Pair<Int, Int>, Int>) {
            for (j in 0..<MAP_HEIGHT) {
                for (i in 0..<MAP_WIDTH) {
                    val robotsAtPosition = finalRobotsCountByPosition[Pair(i, j)] ?: 0
                    if (robotsAtPosition == 0) {
                        print(".")
                    } else {
                        print(robotsAtPosition)
                    }
                }
                print("\n")
            }
        }

        private fun parseInput(input: String): List<RobotConf> {
            val positionVelocityRegex = """p=(-?\d+),(-?\d+)\s+v=(-?\d+),(-?\d+)""".toRegex()
            val splitInput = input.split("\n")
            return splitInput.map {
                val result = positionVelocityRegex.find(it)!!.groupValues
                return@map RobotConf(result[1].toInt(), result[2].toInt(), result[3].toInt(), result[4].toInt())
            }
        }
    }

    data class RobotConf(val startPosX: Int, val startPosY: Int, val velocityX: Int, val velocityY: Int)
}