import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 6")

    // Setup - Load the fish
    val input = File("day6input").readLines()[0].split(",").map { it.toInt() }
    println("There are ${input.size} fish")

    val fish = ArrayDeque((0..8).map { 0L }.toList())
    input.forEach { fish[it]++ }

    // Part 1 - Simulate 80 days.
    repeat(80) { iterateFish(fish) }
    println("After part 1 there are ${fish.sum()} fish")

    // Part 2 - Simulate 256 days.
    repeat(256 - 80) { iterateFish(fish) }
    println("After part 2 there are ${fish.sum()} fish")
}

fun iterateFish(fish: ArrayDeque<Long>) {
    fish.addLast(fish.removeFirst())
    fish[6] += fish.last()
}