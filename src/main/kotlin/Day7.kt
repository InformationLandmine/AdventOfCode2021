import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    println("2021 Advent of Code day 7")

    // Setup - Load the positions
    val positions = File("day7input").readLines()[0].split(",").map { it.toInt() }
    println("There are ${positions.size} starting positions")

    val maxPos = positions.maxOrNull()?:0
    val minPos = positions.minOrNull()?:0

    val part1 = (minPos..maxPos).minOf { pos -> positions.sumOf { abs(it - pos) } }
    println("The best position uses $part1 fuel")

    val part2 = (minPos..maxPos).minOf { pos -> positions.sumOf { abs(it - pos) * (abs(it - pos) + 1) / 2 } }
    println("The best position uses $part2 fuel")
}
