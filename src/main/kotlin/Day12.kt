import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 12")

    // Setup - Load the cave connections.
    val LINES_REGEX = """(.*)-(.*)""".toRegex()
    val paths = File("day12input").readLines().map { with(LINES_REGEX.matchEntire(it)!!) {
            Pair(groups[1]?.value!!, groups[2]?.value!!)
        }
    }

    // Build the cave map.
    val nodes = paths.flatMap { listOf(CaveNode(it.first, it.first.contains("[a-z]".toRegex())),
        CaveNode(it.second, it.second.contains("[a-z]".toRegex()))) }.toSet()

    paths.forEach { cave ->
        nodes.first { it.name == cave.first }.connections.add(nodes.first { it.name == cave.second })
        nodes.first { it.name == cave.second }.connections.add(nodes.first { it.name == cave.first })
    }

    // Part 1 - find all paths through the cave
    val part1 = getRoutes(nodes.first { it.name == "start" }, ArrayList<CaveNode>())
    println("Part 1 - there are ${part1.size} routes through the cave")

    // Part 2
    val part2 = getRoutes2(nodes.first { it.name == "start" }, ArrayList<CaveNode>())
    println("Part 2 - there are ${part2.size} routes through the cave")
}

fun getRoutes(node: CaveNode, currentRoute: MutableList<CaveNode>): ArrayList<List<CaveNode>> {
    val routesToEnd = ArrayList<List<CaveNode>>()
    if (node.small && currentRoute.contains(node))
        return routesToEnd

    currentRoute.add(node)
    if (node.name == "end") {
        routesToEnd.add(currentRoute)
        return routesToEnd
    }

    node.connections.forEach { connection -> routesToEnd.addAll(getRoutes(connection, currentRoute.toMutableList())) }
    return routesToEnd
}

fun getRoutes2(node: CaveNode, currentRoute: MutableList<CaveNode>): ArrayList<List<CaveNode>> {
    val routesToEnd = ArrayList<List<CaveNode>>()

    if (node.name == "start" && currentRoute.contains(node))
        return routesToEnd

    if (node.small && currentRoute.contains(node) &&
        (currentRoute.filter { it.small }.count()) > currentRoute.filter { it.small }.distinct().count())
        return routesToEnd

    currentRoute.add(node)
    if (node.name == "end") {
        routesToEnd.add(currentRoute)
        return routesToEnd
    }

    node.connections.forEach { connection -> routesToEnd.addAll(getRoutes2(connection, currentRoute.toMutableList())) }
    return routesToEnd
}

data class CaveNode(val name:String, val small: Boolean) {
    val connections = ArrayList<CaveNode>()
}
