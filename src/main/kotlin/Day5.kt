import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 5")

    // Setup - Read the vent lines.
    val LINES_REGEX = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
    val ventLines = File("day5input").readLines().map { with(LINES_REGEX.matchEntire(it)!!) {
            Line(Point(groups[1]?.value?.toInt()!!, groups[2]?.value?.toInt()!!),
                Point(groups[3]?.value?.toInt()!!, groups[4]?.value?.toInt()!!))
        }
    }

    // Part 1 - Calculate how many of the horizontal and vertical vent lines overlap.
    val straightLines = ventLines.filter { (it.first.x == it.second.x) or (it.first.y == it.second.y) }
    val part1 = straightLines.flatMap { it.getPoints() }.groupBy { it }.count { it.value.size > 1 }
    println("The horizontal and vertical vent lines overlap at $part1 points")

    // Part 2 - Calculate how many of the vent lines overlap.
    val part2 = ventLines.flatMap { it.getPoints() }.groupBy { it }.count { it.value.size > 1 }
    println("All the vent lines overlap at $part2 points")
}

data class Point(val x:Int, val y:Int)
typealias Line = Pair<Point, Point>

fun Line.getPoints(): List<Point> {
    val dx = if (first.x < second.x) 1 else if (first.x > second.x) -1 else 0
    val dy = if (first.y < second.y) 1 else if (first.y > second.y) -1 else 0
    val result = ArrayList<Point>()
    var newPoint = first
    result.add(newPoint)
    while (newPoint != second) {
        newPoint = Point(newPoint.x + dx, newPoint.y + dy)
        result.add(newPoint)
    }
    return result.toList()
}
