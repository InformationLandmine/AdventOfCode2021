import java.io.File
import kotlin.math.pow

typealias Display = List<List<Set<Char>>>

fun main(args: Array<String>) {
    println("2021 Advent of Code day 8")

    // Setup - Load the displays
    val displays = File("day8input").readLines().map {
        it.split('|').map { it.trim().split(' ').map { it.toSet() } }
    }
    println("There are ${displays.size} displays")

    // Part 1 - count the number of 1, 4, 7, and 8 digits in the output displays
    val part1 = displays.sumOf { display -> display[1].count { (it.size <= 4) or (it.size == 7) } }
    println("There are $part1 output digits that are 1, 4, 7, or 8")

    // Part 2 - solve the displays
    val part2 = displays.sumOf { it.value() }
    println("The sum of the displays is $part2")
}

fun Display.value(): Int {
    var result = 0
    val signals = this[0]
    val output = this[1]

    val one = signals.first { it.size == 2 }
    val four = signals.first { it.size == 4 }

    // The first pass at this logic was super messy. I got the idea to just use the '1' and '4' digits to
    // solve the rest from a discussion about the problem, so I used that to clean up my answer :)
    output.forEachIndexed { i, digit ->
        val value = when (digit.size) {
            2 -> 1
            3 -> 7
            4 -> 4
            7 -> 8
            5 -> {
                if (digit.subtract(one).size == digit.size - 2) 3       // if it shares both segments with 1, it is 3
                else if (digit.subtract(four).size == digit.size - 2) 2 // if it shares two segments with 4, it is 2
                else 5
            }
            6 -> {
                if (digit.subtract(one).size == digit.size - 1) 6       // if it shares one segment with 1, it is 6
                else if (digit.subtract(four).size == digit.size - 4) 9 // if it shares four segments with 4, it is 9
                else 0
            }
            else -> 0
        }
        result += value * 10.0.pow(3 - i).toInt()
    }
    return result
}