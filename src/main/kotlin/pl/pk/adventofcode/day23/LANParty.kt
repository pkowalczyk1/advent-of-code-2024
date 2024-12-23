package pl.pk.adventofcode.day23

class LANParty {

    companion object {

        // for part 2
        fun getPartyPassword(input: String): String {
            val adjacentNodes = parseInput(input)
            return getCliqueWithTheMostNodes(adjacentNodes).toList()
                .sorted()
                .joinToString(",")
        }

        // for part 2
        private fun getCliqueWithTheMostNodes(adjacentNodes: Map<String, Set<String>>): Set<String> {
            val cliques = runBronKerbosch(mutableSetOf(), adjacentNodes.keys.toMutableSet(), mutableSetOf(), adjacentNodes)
            return cliques.maxBy { it.size }
        }

        private fun runBronKerbosch(currentClique: MutableSet<String>, nodesToProcess: MutableSet<String>,
                                    nodesNotInClique: MutableSet<String>, graph: Map<String, Set<String>>): Set<Set<String>> {
            val cliques = mutableSetOf<Set<String>>()
            if (nodesToProcess.isEmpty() && nodesNotInClique.isEmpty()) {
                cliques.add(currentClique.toSet())
            }

            for (node in nodesToProcess.toSet()) {
                val newCurrentClique = currentClique.union(setOf(node)).toMutableSet()
                val newNodesToProcess = nodesToProcess.intersect(graph[node]!!).toMutableSet()
                val newNodesNotInClique = nodesNotInClique.intersect(graph[node]!!).toMutableSet()
                cliques.addAll(runBronKerbosch(newCurrentClique, newNodesToProcess, newNodesNotInClique, graph))
                nodesToProcess.remove(node)
                nodesNotInClique.add(node)
            }

            return cliques
        }

        // for part 1
        fun getNumberOfTriplesWithTHost(input: String): Int {
            val adjacentNodes = parseInput(input)
            val foundTriples = mutableSetOf<MutableSet<String>>()
            for ((host, neighbours) in adjacentNodes.entries) {
                if (host.startsWith("t")) {
                    val neighboursList = neighbours.toList()
                    for (i in 0..<neighboursList.size - 1) {
                        for (j in i + 1..<neighboursList.size) {
                            val secondHost = neighboursList[i]
                            val thirdHost = neighboursList[j]
                            val secondHostNeighbours = adjacentNodes[secondHost]!!
                            val thirdHostNeighbours = adjacentNodes[thirdHost]!!
                            if (secondHostNeighbours.contains(thirdHost) && thirdHostNeighbours.contains(secondHost)) {
                                val triple = mutableSetOf(host, secondHost, thirdHost)
                                if (!foundTriples.contains(triple)) {
                                    foundTriples.add(triple)
                                }
                            }
                        }
                    }
                }
            }

            return foundTriples.size
        }

        private fun parseInput(input: String): Map<String, Set<String>> {
            val adjacentNodes = mutableMapOf<String, MutableSet<String>>()
            input.split("\n")
                .forEach {
                    val hostsInEdge = it.split("-")
                    val firstAdjacentNodes = adjacentNodes.getOrPut(hostsInEdge[0]) { mutableSetOf() }
                    val secondAdjacentNods = adjacentNodes.getOrPut(hostsInEdge[1]) { mutableSetOf() }
                    firstAdjacentNodes.add(hostsInEdge[1])
                    secondAdjacentNods.add(hostsInEdge[0])
                }
            return adjacentNodes
        }
    }
}