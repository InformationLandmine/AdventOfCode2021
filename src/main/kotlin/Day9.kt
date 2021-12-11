import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 9")

    // Setup - Load the terrain.
    val heightMap = File("day9input").readLines().mapIndexed { row, line ->
        line.mapIndexed { col, it -> mapPoint(it.toString().toInt(), Point(row, col),false) }
    }

    // Part 1 - find the low spots.
    val lowPoints = ArrayList<mapPoint>()
    var riskLevel = 0
    heightMap.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, _ ->
            if (heightMap.getNeighbors(rowIndex, colIndex).filter { it.height <= heightMap[rowIndex][colIndex].height }.count() == 0) {
                riskLevel += heightMap[rowIndex][colIndex].height + 1
                lowPoints.add(heightMap[rowIndex][colIndex])
            }
        }
    }
    println("The sum of the risk level is $riskLevel")

    // Part 2 - find the size of the basins.
    val part2 = lowPoints.map { heightMap.getBasinSize(it) }.sorted().takeLast(3).fold(1, { acc, size -> acc * size })
    println("The product of the three largest basins is $part2")
}

class mapPoint(val height: Int, val loc: Point, var visited: Boolean)

fun List<List<mapPoint>>.getNeighbors(row: Int, col: Int): List<mapPoint> {
    val result = ArrayList<mapPoint>()
    if (row > 0)result.add(this[row-1][col])
    if (row < this.size - 1) result.add(this[row+1][col])
    if (col > 0) result.add(this[row][col-1])
    if (col < this[row].size - 1) result.add(this[row][col+1])
    return result
}

fun List<List<mapPoint>>.getBasinSize(point: mapPoint): Int {
    var result = 0
    if (!point.visited && point.height < 9) result++
    point.visited = true
    return result + getNeighbors(point.loc.x, point.loc.y).filter { !it.visited && it.height < 9  }.sumOf { getBasinSize(it) }
}