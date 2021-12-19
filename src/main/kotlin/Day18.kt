import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 18")

    // Setup - Load the snailfish numbers
    val snailfish = File("day18input").readLines()
    println("There are ${snailfish.size} snailfish numbers")

    // Part 1 -
    val part1 = magnitude(snailfish.reduce { acc, s -> addSnailfish(acc, s) })
    println("Part 1 - The magnitude of the added snailfish numbers is $part1")

    // Part 2 -
    val part2 = snailfish.map { sf1 -> snailfish.map { sf2 -> magnitude(addSnailfish(sf1, sf2)) }.maxOf { it } }.maxOf { it }
    println("Part 2 - The maximum magnitude from adding any of the two snailfish numbers is $part2")
}

val splitRegex = """\d\d""".toRegex()
val numberRegex = """\d+""".toRegex()
val pairRegex = """\[\d+,\d+\]""".toRegex()

fun addSnailfish(val1: String, val2: String): String {
    var result = "[$val1,$val2]"
    while (needsExplode(result) || needsSplit(result)) {
        if (needsExplode(result)) result = explode(result)
        else if (needsSplit(result)) result = split(result)
    }
    return result
}

fun magnitude(sf: String): Int {
    var result = reduceSnailfish(sf)
    while (result.contains('[')) result = reduceSnailfish(result)
    return result.toInt()
}

fun reduceSnailfish(sf:String): String {
    pairRegex.find(sf)?.let {
        val values =  pairValues(it.value)
        val reduced = 3 * values.first + 2 * values.second
        return sf.take(it.range.first) + reduced.toString() + sf.drop(it.range.last + 1)
    }
    return sf
}

fun deepPairIndex(sf:String): Int {
    var result = 0
    var depth = 0
    for (i in 0 until sf.length) {
        when (sf[i]) {
            '[' -> ++depth
            ']' -> --depth
            else -> depth
        }
        if (depth > 4) {
            result = i
            break
        }
    }
    return result
}

fun needsSplit(sf: String) = sf.contains(splitRegex)
fun needsExplode(sf: String) = (deepPairIndex(sf) > 0)

fun pairValues(pair: String): Pair<Int, Int> {
    val values = numberRegex.findAll(pair)
    return Pair(values.first().value.toInt(), values.last().value.toInt())
}

fun split(sf: String): String {
    splitRegex.find(sf)?.let {
        val newPair = "[${it.value.toInt() / 2},${(it.value.toInt() + 1) / 2}]"
        return sf.replaceFirst(it.value, newPair)
    }
    return sf
}

fun explode(sf: String): String {
    // Get first too-deep pair and its values.
    val pairIndex = deepPairIndex(sf)
    val pair = pairRegex.find(sf, pairIndex)!!
    val pairVals = pairValues(pair.value)
    var result = sf

    // find and replace the right number.
    numberRegex.find(sf, pair.range.last)?.let {
        val newVal = it.groupValues.first().toInt() + pairVals.second
        result = result.take(it.range.first) + newVal.toString() + result.drop(it.range.last + 1)
    }

    // Replace the exploded pair with 0.
    result = result.take(pair.range.first) + "0" + result.drop(pair.range.last + 1)

    // find and replace the left number.
    numberRegex.findAll(sf.take(pairIndex)).lastOrNull()?.let {
        val newVal = it.groupValues.first().toInt() + pairVals.first
        result = result.take(it.range.first) + newVal.toString() + result.drop(it.range.last + 1)
    }

    return result
}
