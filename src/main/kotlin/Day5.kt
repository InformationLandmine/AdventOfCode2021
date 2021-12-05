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

    // Part 1 - plot the vent map with just horizontal and vertical lines.
    val straightLines = ventLines.filter { (it.first.x == it.second.x) or (it.first.y == it.second.y) }
    straightLines.forEach { vents.plotLine(it) }
    println("The horizontal and vertical lines overlap at ${vents.overlapCount} points")

    // Part 2 - plot the rest of the lines.
    val remainingLines = ventLines.filterNot { (it.first.x == it.second.x) or (it.first.y == it.second.y) }
    remainingLines.forEach { vents.plotLine(it) }
    println("After plotting the remaining lines, they overlap at ${vents.overlapCount} points")

    // Part 3 - alternate method without using VentMap.
    val overlaps = ventLines.flatMap { it.getPoints() }.groupBy { it }.count { it.value.size > 1 }
    println("The new method found overlaps at $overlaps points")
}

data class Point(val x:Int, val y:Int)

fun Pair<Point, Point>.getPoints(): List<Point> {
    val dx = if (this.first.x < this.second.x) 1 else if (this.first.x > this.second.x) -1 else 0
    val dy = if (this.first.y < this.second.y) 1 else if (this.first.y > this.second.y) -1 else 0
    val result = ArrayList<Point>()
    var newPoint = this.first
    result.add(newPoint)
    while (newPoint != this.second) {
        newPoint = Point(newPoint.x + dx, newPoint.y + dy)
        result.add(newPoint)
    }
    return result.toList()
}

class VentMap() {
    val vents = HashMap<Point, Int>()
    val overlapCount get() = vents.count { it.value > 1 }

    fun plotLine(line: Pair<Point, Point>) {
        line.getPoints().forEach { vents[it] = (vents[it] ?: 0) + 1 }
    }
}