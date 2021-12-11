import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("2021 Advent of Code day 1")

    val input = File("day1input").readLines().map { it.toInt() }
    println("There are ${input.size} measurements")

    // Part 1
    var timeMs = measureTimeMillis {
        val part1Answer = input.windowed(2).count { it[1] > it[0] }
        println("$part1Answer measurements were larger than the previous one.")
    }
    println("Part 1 solution took $timeMs ms")

    // Part 2
    timeMs = measureTimeMillis {
        // Since the two middle terms are the same, just compare the nth to the n+3rd term.
        val part2Answer = input.windowed(4).count { it[3] > it[0] }
        println("$part2Answer measurements were larger than the previous one.")
    }
    println("Part 2 solution took $timeMs ms")
}
