import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 5")

    // Setup - Read the vent lines.
    val LINES_REGEX = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
    val ventLines = ArrayList<Pair<Point, Point>>()
    File("day5input").forEachLine {
        val parse = LINES_REGEX.matchEntire(it)
        val p1 = Point(parse?.groups?.get(1)?.value?.toInt()!!, parse?.groups?.get(2)?.value?.toInt()!!)
        val p2 = Point(parse?.groups?.get(3)?.value?.toInt()!!, parse?.groups?.get(4)?.value?.toInt()!!)
        ventLines.add(Pair(p1, p2))
    }

    // Create the vent map.
    val vents = VentMap()

    // Part 1 - plot the terrain map with just horizontal and vertical lines.
    val straightLines = ventLines.filter { (it.first.x == it.second.x) or (it.first.y == it.second.y) }
    straightLines.forEach { vents.plotLine(it) }
    println("The horizontal and vertical lines overlap at ${vents.overlapCount} points")

    // Part 2 - plot the rest of the lines.
    val remainingLines = ventLines.filterNot {(it.first.x == it.second.x) or (it.first.y == it.second.y) }
    remainingLines.forEach { vents.plotLine(it) }
    println("After plotting the remaining lines, they overlap at ${vents.overlapCount} points")
}

data class Point(val x:Int, val y:Int)

class VentMap() {
    val vents = HashMap<Point, Int>()
    val overlapCount get() = vents.count { it.value > 1 }

    fun plotLine(line: Pair<Point, Point>) {
        val dx = if (line.first.x < line.second.x) 1 else if (line.first.x > line.second.x) -1 else 0
        val dy = if (line.first.y < line.second.y) 1 else if (line.first.y > line.second.y) -1 else 0
        var plotPoint = line.first
        while (plotPoint != line.second) {
            vents[plotPoint] = (vents[plotPoint] ?: 0) + 1
            plotPoint = Point(plotPoint.x + dx, plotPoint.y + dy)
        }
        vents[plotPoint] = (vents[plotPoint] ?: 0) + 1
    }
}