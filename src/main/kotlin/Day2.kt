import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 2")

    // Setup - Read the list of directions.
    val DIRECTIONS_REGEX = """(\S+) (\d+)""".toRegex()
    val directions = ArrayList<Pair<String, Long>>()
    File("day2input").forEachLine {
        val parse = DIRECTIONS_REGEX.matchEntire(it)
        val direction = parse?.groups?.get(1)?.value?:"?"
        val count = parse?.groups?.get(2)?.value?.toLong()?:0L
        directions.add (Pair(direction, count))
    }
    println("There are ${directions.size} direction commands")

    // Part 1 - follow the directions
    var position = 0L
    var depth = 0L

    directions.forEach {
        when (it.first) {
            "forward" -> position += it.second
            "up" -> depth -= it.second
            "down" -> depth += it.second
        }
    }
    println("After following the directions, position = $position; depth = $depth; product = ${position * depth}")

    // Part 2 - follow the new interpretation of the directions
    position = 0L
    depth = 0L
    var aim = 0L

    directions.forEach {
        when (it.first) {
            "forward" -> {
                position += it.second
                depth += aim * it.second
            }
            "up" -> aim -= it.second
            "down" -> aim += it.second
        }
    }
    println("After following the new directions, position = $position; depth = $depth; product = ${position * depth}")
}
