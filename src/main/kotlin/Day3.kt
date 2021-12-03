import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 3")

    // Setup - Read the list of directions.
    val input = ArrayList<String>()

    File("day3input").forEachLine { input.add(it) }
    println("There are ${input.size} diagnostic readings")

    // Part 1 - find the most common bits and least common bits
    var gamma = ""
    var epsilon = ""
    repeat (input[0].length) { i ->
        gamma += if (input.count { it[i] == '0' } > input.count { it[i] == '1' }) '0' else '1'
        when (gamma[i]) { '0' -> epsilon += '1'; '1' -> epsilon += '0' }
    }
    println("Part 1: gamma * epsilon = ${gamma.toInt(2) * epsilon.toInt(2)}")

    // Part 2 - find the O2 generator and CO2 scrubber ratings
    var o2Generator = input.toList()
    var co2Scrubber = input.toList()

    // O2
    repeat (input[0].length) { i ->
        if (o2Generator.size > 1) {
            when (o2Generator.count { it[i] == '0' } > o2Generator.count { it[i] == '1' }) {
                true -> o2Generator = o2Generator.filter { it[i] == '0' }
                false -> o2Generator = o2Generator.filter { it[i] == '1' }
            }
        }
        if (co2Scrubber.size > 1) {
            when (co2Scrubber.count { it[i] == '0' } > co2Scrubber.count { it[i] == '1' }) {
                true -> co2Scrubber = co2Scrubber.filter { it[i] == '1' }
                false -> co2Scrubber = co2Scrubber.filter { it[i] == '0' }
            }
        }
    }
    println("Part 2: o2rating * co2rating = ${o2Generator[0].toInt(2) * co2Scrubber[0].toInt(2)}")
}