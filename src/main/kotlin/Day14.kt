import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("2021 Advent of Code day 14")

    // Setup - Load the polymers.
    val RULES_REGEX = """(\S\S) -> (\S)""".toRegex()
    val input = File("day14input").readLines()
    val template = input.take(1).first().toString()
    val rules = input.drop(2).map { with(RULES_REGEX.matchEntire(it)!!) { Pair(groups[1]?.value!!, groups[2]?.value!!) } }

    // Make a set of all possible elements and create a map to track pair counts. Make a more useful rules map.
    val elements = rules.flatMap { rule -> rule.first.toSet() }.toSet().associateWith { 0L }.toMutableMap()
    val pairCounts = elements.keys
        .flatMap { elem1 -> elements.keys.map { elem2 -> elem1.toString() + elem2.toString() } }
        .associateWith { 0L }
        .toMutableMap()
    val rulesToPairs = rules.associate { rule ->
        Pair(rule.first, listOf("${rule.first[0]}${rule.second}", "${rule.second}${rule.first[1]}"))
    }

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

/*
 Explanation

 Read the input, storing the template string and reading the rules as a `Pair<String, String>`, example `<"CH", "B">`
 Get a unique set of all elements using rule.first from the rules pairs, since each element has to be represented in
 the rules. For getting the final answer, make it a map where the value will be the count in the final chain.
 So that gives us `elements = Map<Char, Long>` containing all the single letter elements and a way to store counts later.

 The next two maps are the secret sauce for processing the insertion iterations.
 First, create a better rules map `rulesToPairs = Map<String, List<String>>` telling us which two new element pairs will
 be created when following a rule. So instead of the first rule looking like `<"CH", "B">`, it will now be `<"CH", {"CB", "BH"}>`
 Second, create a map of all possible element pairs with their counts in the polymer chain as the values.
 Do this by nested iteration over `elements`, so we end up with `pairCounts = Map<String, Long>`, example `<"HC", 0>`
 Finally, before iterating, initialize `pairCounts` counts from the template string. So "NN", "NC", and "CB" will all be 1 and the rest 0.

 All that setup is most of the work! Time to iterate the insertions.
 Start by copying the current `pairCounts` to `prevCounts` since we need the initial state preserved to correctly insert.
 For each key (element pair) in `prevCounts`, subtract its previous count from the current count in `pairCounts` -
 all of these element pairs are being replaced with two new ones (they overlap). "CH" will become "CBH" which has pairs
 "CB" and "BH". Increment the `pairCounts` of the two new pairs by the `prevCount` of the "parent" pair.

 That's it! Do that as many times as desired, such as 10 or 40 :)

 Finally, to get the answer, the `pairCounts` have to be transformed to individual element counts, so count them up.
 `<"NC", 4366>` will increment both `elements["N"]` and `elements["C"]` by 4366.
 Since the pairs overlap, all the element counts are doubled, except the first and last one in the original template.
 But integer division fixes that! So add 1 to all the counts then divide by 2.

 Ta-da!
*/
