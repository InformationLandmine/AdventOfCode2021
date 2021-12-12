import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 12")

    // Setup - Load the cave connections.
    val CONNECTIONS_REGEX = """(.*)-(.*)""".toRegex()
    val paths = File("day12input").readLines()
        .map { with(CONNECTIONS_REGEX.matchEntire(it)!!) { Pair(groups[1]?.value!!, groups[2]?.value!!) } }

    // Build the cave map.
    val nodes = paths.flatMap { listOf(CaveNode(it.first), CaveNode(it.second)) }.toSet()

    paths.forEach { path ->
        nodes.first { it.name == path.first }.connections.add(nodes.first { it.name == path.second })
        nodes.first { it.name == path.second }.connections.add(nodes.first { it.name == path.first })
    }

    // Part 1 - find all paths through the cave
    val part1 = nodes.first { it.name == "start" }.getRoutes(ArrayList<CaveNode>())
    println("Part 1 - there are ${part1.size} routes through the cave")

    // Part 2
    val part2 = nodes.first { it.name == "start" }.getRoutes(ArrayList<CaveNode>(), 1)
    println("Part 2 - there are ${part2.size} routes through the cave")
}

class CaveNode(val name:String) {
    val small = name.contains("[a-z]".toRegex())
    val connections = ArrayList<CaveNode>()

    fun getRoutes(currentRoute: MutableList<CaveNode>, maxSmallRevisits: Int = 0): ArrayList<List<CaveNode>> {
        val routesToEnd = ArrayList<List<CaveNode>>()

        if (name == "start" && currentRoute.size > 0)
            return routesToEnd

        if (small && currentRoute.contains(this) &&
            (currentRoute.count { it.small }) >= currentRoute.filter { it.small }.distinct().count() + maxSmallRevisits)
            return routesToEnd

        currentRoute.add(this)

        if (name == "end")
            return routesToEnd.apply { add(currentRoute) }

        connections.forEach { routesToEnd.addAll(it.getRoutes(currentRoute.toMutableList(), maxSmallRevisits)) }
        return routesToEnd
    }
}
