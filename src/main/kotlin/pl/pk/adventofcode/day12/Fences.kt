package pl.pk.adventofcode.day12

class Fences {

    companion object {

        fun getFenceCost(map: String): Long {
            val mapArray = parseInputIntoArray(map)
            val rows = mapArray.size
            val columns = mapArray[0].size
            val processedFields = Array (rows) { BooleanArray (columns) }
            val fieldsToBeginProcessing = ArrayDeque<Pair<Int, Int>>()
            fieldsToBeginProcessing.addFirst(Pair(0, 0))

            var totalCost = 0L

            while (fieldsToBeginProcessing.isNotEmpty()) {
                var fieldsCount = 0L
                val processingQueue = ArrayDeque<Pair<Int, Int>>()
                val fieldIndex = fieldsToBeginProcessing.removeLast()
                val plantType = mapArray[fieldIndex.first][fieldIndex.second]
                val rowSideUpperColumns = mutableMapOf<Int, MutableList<Int>>()
                val rowSideLowerColumns = mutableMapOf<Int, MutableList<Int>>()
                val columnSideUpperRows = mutableMapOf<Int, MutableList<Int>>()
                val columnSideLowerRows = mutableMapOf<Int, MutableList<Int>>()
                processingQueue.addFirst(fieldIndex)

                while (processingQueue.isNotEmpty()) {
                    val (row, column) = processingQueue.removeLast()
                    if (!processedFields[row][column]) {
                        processedFields[row][column] = true
                        fieldsCount++
                        getBordersCountAndUpdateQueuesForNeighbour(row + 1, column, rows, columns, mapArray, plantType,
                            processedFields, processingQueue, fieldsToBeginProcessing, rowSideUpperColumns, rowSideLowerColumns,
                            columnSideUpperRows, columnSideLowerRows, "ROW_U")
                        getBordersCountAndUpdateQueuesForNeighbour(row, column + 1, rows, columns, mapArray, plantType,
                            processedFields, processingQueue, fieldsToBeginProcessing, rowSideUpperColumns, rowSideLowerColumns,
                            columnSideUpperRows, columnSideLowerRows, "COL_U")
                        getBordersCountAndUpdateQueuesForNeighbour(row - 1, column, rows, columns, mapArray, plantType,
                            processedFields, processingQueue, fieldsToBeginProcessing, rowSideUpperColumns, rowSideLowerColumns,
                            columnSideUpperRows, columnSideLowerRows, "ROW_L")
                        getBordersCountAndUpdateQueuesForNeighbour(row, column - 1, rows, columns, mapArray, plantType,
                            processedFields, processingQueue, fieldsToBeginProcessing, rowSideUpperColumns, rowSideLowerColumns,
                            columnSideUpperRows, columnSideLowerRows, "COL_L")
                    }
                }

                var rowSides = 0
                if (rowSideUpperColumns.isNotEmpty()) {
                    rowSides += rowSideUpperColumns.values.map { getSidesFromList(it) }
                        .reduce { a, b -> a + b }
                }

                var columnSides = 0
                if (columnSideUpperRows.isNotEmpty()) {
                    columnSides += columnSideUpperRows.values.map { getSidesFromList(it) }
                        .reduce { a, b -> a + b }
                }

                if (rowSideLowerColumns.isNotEmpty()) {
                    rowSides += rowSideLowerColumns.values.map { getSidesFromList(it) }
                        .reduce { a, b -> a + b }
                }

                if (columnSideLowerRows.isNotEmpty()) {
                    columnSides += columnSideLowerRows.values.map { getSidesFromList(it) }
                        .reduce { a, b -> a + b }
                }

                totalCost += fieldsCount * (rowSides + columnSides)
            }

            return totalCost
        }

        private fun getSidesFromList(list: MutableList<Int>): Int {
            list.sort()
            var sides = 1
            var previous = list[0] - 1
            for (value in list) {
                if (value != previous + 1) {
                    sides++
                }

                previous = value
            }
            return sides
        }

        private fun getBordersCountAndUpdateQueuesForNeighbour(row: Int, column: Int, rows: Int, columns: Int,
                                                               mapArray: List<CharArray>, plantType: Char,
                                                               processedFields: Array<BooleanArray>,
                                                               processingQueue: ArrayDeque<Pair<Int, Int>>,
                                                               fieldsToBeginProcessing: ArrayDeque<Pair<Int, Int>>,
                                                               rowSideUpperColumns: MutableMap<Int, MutableList<Int>>,
                                                               columnSideUpperRows: MutableMap<Int, MutableList<Int>>,
                                                               rowSideLowerColumns: MutableMap<Int, MutableList<Int>>,
                                                               columnSideLowerRows: MutableMap<Int, MutableList<Int>>,
                                                               neighbourType: String) {
            if (row in 0..<rows && column in 0..<columns) {
                if (mapArray[row][column] == plantType && !processedFields[row][column]) {
                    processingQueue.addFirst(Pair(row, column))
                }

                if (mapArray[row][column] != plantType) {
                    updateCorrectSidesMap(neighbourType, row, column, rowSideUpperColumns, rowSideLowerColumns,
                        columnSideUpperRows, columnSideLowerRows)
                    if (!processedFields[row][column]) {
                        fieldsToBeginProcessing.addFirst(Pair(row, column))
                    }
                }
            } else {
                updateCorrectSidesMap(neighbourType, row, column, rowSideUpperColumns, rowSideLowerColumns,
                    columnSideUpperRows, columnSideLowerRows)
            }
        }

        private fun updateCorrectSidesMap(
            neighbourType: String,
            row: Int,
            column: Int,
            rowSideUpperColumns: MutableMap<Int, MutableList<Int>>,
            rowSideLowerColumns: MutableMap<Int, MutableList<Int>>,
            columnSideUpperRows: MutableMap<Int, MutableList<Int>>,
            columnSideLowerRows: MutableMap<Int, MutableList<Int>>) {
            when (neighbourType) {
                "ROW_U" -> {
                    rowSideUpperColumns.putIfAbsent(row, mutableListOf())
                    rowSideUpperColumns[row]!!.add(column)
                }

                "ROW_L" -> {
                    rowSideLowerColumns.putIfAbsent(row, mutableListOf())
                    rowSideLowerColumns[row]!!.add(column)
                }

                "COL_U" -> {
                    columnSideUpperRows.putIfAbsent(column, mutableListOf())
                    columnSideUpperRows[column]!!.add(row)
                }

                "COL_L" -> {
                    columnSideLowerRows.putIfAbsent(column, mutableListOf())
                    columnSideLowerRows[column]!!.add(row)
                }
            }
        }

        private fun parseInputIntoArray(map: String): List<CharArray> {
            val mapArray = map.split("\n")
                .map { it.toCharArray() }
            return mapArray
        }
    }
}
