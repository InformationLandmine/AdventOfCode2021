import java.io.File
import java.util.PriorityQueue
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    println("2021 Advent of Code day 15")

    // Setup - Load the cave risk map.
    val rawInput = File("day15input").readLines()
        .map { row -> row.map { col -> Character.getNumericValue(col) }.toMutableList() }.toMutableList()

    // Save the initial size for part 1.
    val initialSize = Point(rawInput.size, rawInput.first().size)

    // Extend the map to five times the original size, incrementing the risk of each location as specified.
    rawInput.forEach { row ->
        val initial = row.toList()
        repeat(4) { i ->
            row.addAll(initial.map { if (it + i + 1 < 10) (it + i + 1) else (it + i + 1 - 9) })
        }
    }
    val oneSet = rawInput.toList()
    repeat(4) { i ->
        oneSet.forEach { row ->
            rawInput.add(row.map { if (it + i + 1 < 10) (it + i + 1) else (it + i + 1 - 9) }.toMutableList())
        }
    }
    val caveMap = rawInput.mapIndexed() { row, line -> line.mapIndexed { col, it ->
        CavePoint(Point(row, col), it) }.toMutableList() }.toMutableList()

    // Get all the neighbors for each location.
    caveMap.flatten().forEach { spot ->
        spot.neighbors = caveMap.getNeighbors(spot.loc.x, spot.loc.y)
    }

    // Implement Dijkstra's algorithm for least cost path.
    val compareByRisk: Comparator<CavePoint> = compareBy { it.risk }
    val pq = PriorityQueue(compareByRisk)
    pq.add(caveMap[0][0].apply { totalRisk = this.risk })

    while (pq.isNotEmpty()) {
        val current = pq.poll()!!
        current.neighbors.forEach { neighbor ->
            if (neighbor.totalRisk > (current.totalRisk + neighbor.risk)) {
                if (neighbor.totalRisk != Int.MAX_VALUE) {
                    pq.remove(neighbor)
                }
                neighbor.totalRisk = current.totalRisk + neighbor.risk
                pq.add(neighbor)
            }
        }
    }
    val leastRisk1 = caveMap[initialSize.x-1][initialSize.y-1].totalRisk - caveMap.first().first().risk
    val leastRisk2 = caveMap.last().last().totalRisk - caveMap.first().first().risk
    println("Part 1 - the least total risk is $leastRisk1")
    println("Part 2 - the least total risk is $leastRisk2")
}

class CavePoint(val loc: Point, val risk: Int, var totalRisk: Int = Int.MAX_VALUE) {
    var neighbors = ArrayList<CavePoint>()
}

fun List<List<CavePoint>>.getNeighbors(row: Int, col: Int): ArrayList<CavePoint> {
    val result = ArrayList<CavePoint>()
    if (row > 0) result.add(this[row-1][col])                   // top
    if (row < size - 1) result.add(this[row+1][col])            // bottom
    if (col > 0) result.add(this[row][col-1])                   // left
    if (col < this[row].size - 1) result.add(this[row][col+1])  // right
    return result
}