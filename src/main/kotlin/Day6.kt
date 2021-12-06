import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 6")

    // Setup - Load the fish
    val fishInput = File("day6input").readLines()[0].split(",").map { it.toInt() }
    println("There are ${fishInput.size} fish")
    val testInput = listOf(3, 4, 3, 1, 2)
    val input = fishInput

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
    val reproducingFish = fish.removeFirst()
    fish.addLast(reproducingFish)
    fish[6] += reproducingFish
}