import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 10")

    // Setup - Load the navigation subsystem
    val input = File("day10input").readLines()
    val scoreTable1 = mapOf(Pair(')', 3L), Pair(']', 57L), Pair('}', 1197L), Pair('>', 25137L))
    val scoreTable2 = mapOf(Pair('(', 1L), Pair('[', 2L),  Pair('{', 3L), Pair('<', 4L))
    val matchMap = mapOf(Pair(')', '('), Pair(']', '['), Pair('}', '{'), Pair('>', '<'))

    // Parts 1 and 2 - find the illegal characters and score them; complete the remaining lines and score them.
    var part1Score = 0L
    val part2Scores = ArrayList<Long>()
    input.forEach { line ->
        val groupStack = ArrayDeque<Char>()
        var corruptedLine = false
        line.forEach { char ->
            if (!corruptedLine) {
                when (char) {
                    '(', '[', '{', '<' -> groupStack.addFirst(char)
                    else -> if (groupStack.removeFirst() != matchMap.getValue(char)) {
                        corruptedLine = true
                        part1Score += scoreTable1.getValue(char)
                    }
                }
            }
        }
        if (!corruptedLine) part2Scores.add(groupStack.fold(0L, { acc, it -> acc * 5 + scoreTable2.getValue(it) }))
    }
    part2Scores.sort()
    println("Part 1 score is $part1Score")
    println("Part 2 score is ${part2Scores[part2Scores.size/2]}")
}
