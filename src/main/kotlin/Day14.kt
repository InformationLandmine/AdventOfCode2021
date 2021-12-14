import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("2021 Advent of Code day 14")

    // Setup - Load the polymers.
    val RULES_REGEX = """(\S\S) -> (\S)""".toRegex()
    val input = File("day14input").readLines()
    val template = input.take(1).first().toString()
    val rules = input.drop(2).map { with(RULES_REGEX.matchEntire(it)!!) { Pair(groups[1]?.value!!, groups[2]?.value!!) } }
    val rulesToPairs = rules.associate { rule ->
        Pair(rule.first, listOf("${rule.first[0]}${rule.second}", "${rule.second}${rule.first[1]}"))
    }

    // Make a set of all possible elements and create a map to track pair counts.
    val elements = rules.flatMap { rule -> rule.first.toSet() }.toSet().associateWith { 0L }.toMutableMap()
    val pairCounts = elements.keys
        .flatMap { elem1 -> elements.keys.map { elem2 -> elem1.toString() + elem2.toString() } }
        .associateWith { 0L }
        .toMutableMap()

    // Populate the initial pair counts.
    template.windowed(2).forEach { pairCounts[it] = pairCounts.getValue(it) + 1   }

    val time = measureTimeMillis {
        repeat(40) {
            val prevCounts = pairCounts.toMap()
            prevCounts.keys.forEach { pair ->
                pairCounts[pair] = pairCounts.getValue(pair) - prevCounts.getValue(pair)
                rulesToPairs.getValue(pair).forEach { newPair ->
                    pairCounts[newPair] = pairCounts.getValue(newPair) + prevCounts.getValue(pair)
                }
            }
        }
        // Add up the total elements in the resulting chain.
        pairCounts.forEach { pair ->
            elements[pair.key[0]] = elements.getValue(pair.key[0]) + pair.value
            elements[pair.key[1]] = elements.getValue(pair.key[1]) + pair.value
        }
        // Adjust for each one being double counted (except for the first and last, which integer division fixes)
        elements.keys.forEach { elem ->  elements[elem] = (elements.getValue(elem) + 1) / 2 }
    }
    println("Completed in $time ms")
    println("Most abundant - least abundant is ${elements.maxOf { it.value } - elements.minOf { it.value }}")
}
