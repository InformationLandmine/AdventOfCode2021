import java.io.File
import kotlin.math.abs
import kotlin.math.min

fun main(args: Array<String>) {
    println("2021 Advent of Code day 7")

    // Setup - Load the positions
    val positions = File("day7input").readLines()[0].split(",").map { it.toInt() }
    println("There are ${positions.size} starting positions")

    val maxPos = positions.maxOrNull()?:0
    val minPos = positions.minOrNull()?:0

    val part1 = (minPos..maxPos).map { pos -> positions.sumOf { abs(it - pos)} }.minOrNull()?:Int.MAX_VALUE
    println("The best position uses $part1 fuel")

    val part2 = (minPos..maxPos).map { pos -> positions.sumOf { abs(it - pos) * (abs(it - pos) + 1) / 2} }.minOrNull()?:Int.MAX_VALUE
    println("The best position uses $part2 fuel")
}
