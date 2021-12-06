import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 6")

    // Setup - Read the fish "timers"
    val fishInput = File("day6input").readLines()[0].split(",").map { it.toInt() }
    println("There are ${fishInput.size} fish")
    val testInput = listOf(3, 4, 3, 1, 2)
    val input = testInput

    val fish = arrayOf(
        input.count { it == 0 }.toLong(),
        input.count { it == 1 }.toLong(),
        input.count { it == 2 }.toLong(),
        input.count { it == 3 }.toLong(),
        input.count { it == 4 }.toLong(),
        input.count { it == 5 }.toLong(),
        input.count { it == 6 }.toLong(),
        input.count { it == 7 }.toLong(),
        input.count { it == 8 }.toLong(),
    )

    // Part 1 - Simulate 80 days.
    repeat(80) { iterateFish(fish) }
    println("After part 1 there are ${fish.sum()} fish")

    // Part 2 - Simulate 256 days.
    repeat(256 - 80) { iterateFish(fish) }
    println("After part 2 there are ${fish.sum()} fish")
}

fun iterateFish(fish: Array<Long>) {
    val oldFishZero = fish[0]
    fish[0] = fish[1]
    fish[1] = fish[2]
    fish[2] = fish[3]
    fish[3] = fish[4]
    fish[4] = fish[5]
    fish[5] = fish[6]
    fish[6] = fish[7] + oldFishZero
    fish[7] = fish[8]
    fish[8] = oldFishZero
}