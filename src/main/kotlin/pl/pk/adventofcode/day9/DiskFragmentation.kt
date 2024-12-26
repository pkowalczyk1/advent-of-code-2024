package pl.pk.adventofcode.day9

import pl.pk.adventofcode.day9.DiskFragmentation.MemoryObjectType.FILE
import pl.pk.adventofcode.day9.DiskFragmentation.MemoryObjectType.FREE_SPACE

class DiskFragmentation {

    companion object {

        // for part 2
        fun rearrangeWholeFilesAndGetChecksum(input: String): Long {
            val memory = parseInputForPart2(input)
            val fileBlocks = memory.filter { it.type == FILE }
            val freeBlocks = memory.filter { it.type == FREE_SPACE }
            for (file in fileBlocks.reversed()) {
                for (freeSpace in freeBlocks) {
                    if (freeSpace.position <= file.position && freeSpace.length >= file.length) {
                        file.position = freeSpace.position
                        freeSpace.position += file.length
                        freeSpace.length -= file.length
                    }
                }
            }

            return fileBlocks.sumOf { it.getPositionsSum() * it.id }
        }

        // for part 1
        fun rearrangeAndGetChecksum(input: String): Long {
            val memory = parseInputForPart1(input)
            var leftIndex = 0
            var rightIndex = memory.size - 1
            while (rightIndex > leftIndex) {
                if (memory[leftIndex] != ".") {
                    leftIndex++
                } else {
                    if (memory[rightIndex] == ".") {
                        rightIndex--
                    } else {
                        val tmp = memory[leftIndex]
                        memory[leftIndex] = memory[rightIndex]
                        memory[rightIndex] = tmp
                        leftIndex++
                        rightIndex--
                    }
                }
            }

            var index = 0
            var checksum = 0L
            while (memory[index] != ".") {
                checksum += index * memory[index].toInt()
                index++
            }

            return checksum
        }

        private fun parseInputForPart2(input: String): List<MemoryObject> {
            val resultList = mutableListOf<MemoryObject>()
            var currentFile = 0L
            var position = 0L
            input.toCharArray()
                .forEachIndexed { index, length ->
                    val lengthNum = length.toString().toInt()
                    if (index % 2 == 0) {
                        resultList.addLast(MemoryObject(position, lengthNum.toString().toLong(), currentFile, FILE))
                        currentFile++
                        position += lengthNum
                    } else {
                        resultList.addLast(MemoryObject(position, lengthNum.toString().toLong(), -1, FREE_SPACE))
                        position += lengthNum
                    }
                }

            return resultList
        }

        private fun parseInputForPart1(input: String): MutableList<String> {
            val resultList = mutableListOf<String>()
            var currentFile = 0
            input.toCharArray()
                .forEachIndexed { index, c ->
                    if (index % 2 == 0) {
                        for (i in 1..c.toString().toInt()) {
                            resultList.addLast(currentFile.toString())
                        }
                        currentFile++
                    } else {
                        for (i in 1..c.toString().toInt()) {
                            resultList.addLast(".")
                        }
                    }
                }

            return resultList
        }
    }

    private data class MemoryObject(var position: Long, var length: Long, val id: Long, val type: MemoryObjectType) {

        fun getPositionsSum() = (2 * position + length - 1) * length / 2
    }

    private enum class MemoryObjectType {
        FILE,
        FREE_SPACE
    }
}