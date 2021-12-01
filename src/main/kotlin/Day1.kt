import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("2021 Advent of Code day 1")

    val input = ArrayList<Int>()
    File("day1input").forEachLine { input.add(it.toInt()) }
    println("There are ${input.size} measurements")

    // Part 1
    var timeMs = measureTimeMillis {
        val part1Answer = input.filterIndexed { index, it -> if (index > 0) it > input[index-1] else false }.count()
        println("$part1Answer measurements were larger than the previous one.")
    }
    println("Part 1 solution took $timeMs ms")

    // Part 2
    timeMs = measureTimeMillis {
        val sums = input.mapIndexed { index, it -> if (index < input.size - 2) it + input[index+1] + input[index+2] else 0 }
        val part2Answer = sums.filterIndexed { index, it -> if (index > 0) it > sums[index-1] else false }.count()
        println("$part2Answer measurements were larger than the previous one.")
    }
    println("Part 2 solution took $timeMs ms")
}
