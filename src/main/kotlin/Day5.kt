import java.awt.Point
import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 5")

    // Setup - Read the bingo draws and cards.
    val LINES_REGEX = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
    val lines = ArrayList<Pair<Point, Point>>()
    File("day5input").forEachLine {
        val parse = LINES_REGEX.matchEntire(it)
        val p1 =  Point(parse?.groups?.get(1)?.value?.toInt()!!, parse.groups.get(2)?.value?.toInt()!!)
        val p2 =  Point(parse?.groups?.get(3)?.value?.toInt()!!, parse.groups.get(4)?.value?.toInt()!!)
        lines.add(Pair(p1, p2))
    }
    println("There are ${lines.size} lines")

    // Part 1 - plot the terrain map with just horizontal and vertical lines.
    val terrain = TerrainMap()

    // Get the horizontal and vertical lines
    val hlines = lines.filter { it.first.y == it.second.y }
    val vlines = lines.filter { it.first.x == it.second.x }
    println("There are ${hlines.size} horizontal and ${vlines.size} vertical lines")

    hlines.forEach { terrain.plotHorizontal(it) }
    vlines.forEach { terrain.plotVertical(it) }
    println("The horizontal and vertical lines overlap at ${terrain.overlapCount} points")

    // Part 2 - plot the rest of the diagonal lines.
    lines.removeAll(hlines)
    lines.removeAll(vlines)
    println("${lines.size} lines remain")
    lines.forEach { terrain.plotDiagonal(it) }
    println("After plotting the remaining lines, they overlap at ${terrain.overlapCount} points")
}

class TerrainMap() {
    val terrain = HashMap<Point, Int>()

    val overlapCount get() = terrain.count { it.value > 1 }

    fun plotHorizontal(line: Pair<Point, Point>) {
        if (line.first.x > line.second.x) {
            for (x in line.second.x..line.first.x) {
                val point = Point(x, line.first.y)
                val elevation = terrain[point]?: 0
                terrain[point] = elevation + 1
            }
        } else {
            for (x in line.first.x..line.second.x) {
                val point = Point(x, line.first.y)
                val elevation = terrain[point] ?: 0
                terrain[point] = elevation + 1
            }
        }
    }

    fun plotVertical(line: Pair<Point, Point>) {
        if (line.first.y > line.second.y) {
            for (y in line.second.y..line.first.y) {
                val point = Point(line.first.x, y)
                val elevation = terrain[point]?: 0
                terrain[point] = elevation + 1
            }
        } else {
            for (y in line.first.y..line.second.y) {
                val point = Point(line.first.x, y)
                val elevation = terrain[point] ?: 0
                terrain[point] = elevation + 1
            }
        }
    }

    fun plotDiagonal(line: Pair<Point, Point>) {
        val p1 = if (line.first.x < line.second.x) line.first else line.second
        val p2 = if (line.first.x > line.second.x) line.first else line.second
        val slope = if (p1.y < p2.y) 1 else -1
        var y = p1.y
        for (x in p1.x..p2.x) {
            val point = Point(x, y)
            val elevation = terrain[point] ?: 0
            terrain[point] = elevation + 1
            y += slope
        }
    }
}