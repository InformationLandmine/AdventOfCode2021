import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 3")

    // Setup - Read the list of directions.
    val input = ArrayList<String>()
    val numbits = 12

    File("day3input").forEachLine { input.add(it) }
    println("There are ${input.size} diagnostic readings")

    // Part 1 - find the most common bits and least common bits
    var gammaStr = ""
    var epsilonStr = ""
    for (i in 0 until input[0].length - 1) {
        gammaStr += if (input.count { it[i] == '0' } > input.count { it[i] == '1' }) '0' else '1'
        epsilonStr += if (input.count { it[i] == '0' } < input.count { it[i] == '1' }) '0' else '1'
    }
    val gamma = Integer.parseInt(gammaStr, 2)
    val epsilon = Integer.parseInt(epsilonStr, 2)
    println("Part 1: gamma = $gamma; epsilon = $epsilon; gamma * epsilon = ${gamma * epsilon}")

    // Part 2 - find the O2 generator and CO2 scrubber ratings
    var o2Generator = input.toList()
    var co2Scrubber = input.toList()

    // O2
    o2loop@for (i in 0 until input[0].length - 1) {
        if (o2Generator.count { it[i] == '0' } > o2Generator.count { it[i] == '1' }) {
            o2Generator = o2Generator.filter { it[i] == '0' }
        } else {
            o2Generator = o2Generator.filter { it[i] == '1' }
        }
        if (o2Generator.size <= 1) break@o2loop
    }

    // CO2
    co2loop@for (i in 0 until input[0].length - 1) {
        if (co2Scrubber.count { it[i] == '0' } > co2Scrubber.count { it[i] == '1' }) {
            co2Scrubber = co2Scrubber.filter { it[i] == '1' }
        } else {
            co2Scrubber = co2Scrubber.filter { it[i] == '0' }
        }
        if (co2Scrubber.size <= 1) break@co2loop
    }

    println(o2Generator[0])
    println(co2Scrubber[0])

    val o2rating = Integer.parseInt(o2Generator[0], 2)
    val co2rating = Integer.parseInt(co2Scrubber[0], 2)
    println("Part 2: o2rating = $o2rating; co2rating = $co2rating; o2rating * co2rating = ${o2rating * co2rating}")
}