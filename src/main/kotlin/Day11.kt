import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 11")

    // Setup - Load the terrain.
    val octoGrid = File("day11input").readLines().mapIndexed { row, line ->
        line.mapIndexed { col, energy -> Octopus(Point(row, col), Character.getNumericValue(energy),false) }
    }

    // Iterate the octopuses and count flashes for 100 steps, then continue until they all flash together.
    var flashCount = 0
    var steps = 0
    var solved = false
    while(!solved) {
        // Increase the energy by one unit.
        octoGrid.forEach { row -> row.forEach { octo -> octo.energy++ } }

        // Flash any octopus with energy > 9 until none have enough energy to flash.
        while (octoGrid.sumOf { row -> row.count { octo -> octo.energy > 9 && !octo.flashed } } > 0) {
            octoGrid.forEach { row -> row.filter { octo -> octo.energy > 9 && !octo.flashed }
                .forEach { octo ->
                    octoGrid.getNeighbors(octo.loc.x, octo.loc.y).forEach { octo -> octo.energy++ }
                    octo.flashed = true
                    flashCount++
                }
            }
        }
        if (++steps == 100) println("Part 1: There were $flashCount flashes after 100 steps")

        // Check if every octopus flashed during this step. If so, we are done.
        if (octoGrid.sumOf { row -> row.count { octo -> !octo.flashed } } == 0) {
            println("Part 2: Synchronized flash on step ${steps}")
            solved = true
        }

        // Reset the state of any octopus that flashed.
        octoGrid.forEach { row -> row.filter { octo -> octo.flashed }.forEach { octo -> octo.reset() } }
    }
}

class Octopus(val loc: Point, var energy: Int, var flashed: Boolean) {
    fun reset() { this.energy = 0; this.flashed = false }
}

fun List<List<Octopus>>.getNeighbors(row: Int, col: Int): List<Octopus> {
    val result = ArrayList<Octopus>()
    if (row > 0) result.add(this[row-1][col])                       // top
    if (row < this.size - 1) result.add(this[row+1][col])           // bottom
    if (col > 0) result.add(this[row][col-1])                       // left
    if (col < this[row].size - 1) result.add(this[row][col+1])      // right
    if (row > 0 && col > 0) result.add(this[row-1][col-1])          // top left
    if ((row < this.size - 1) && col > 0) result.add(this[row+1][col-1])           // bottom left
    if (row > 0 && col < this[row].size - 1) result.add(this[row-1][col+1])                       // top right
    if ((row < this.size - 1) && (col < this[row].size - 1)) result.add(this[row+1][col+1])      // bottom right
    return result
}
