import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 2")

    // Setup - Read the list of commands.
    val COMMANDS_REGEX = """(\S+) (\d+)""".toRegex()
    val commands = File("day2input").readLines().map { with(COMMANDS_REGEX.matchEntire(it)!!) {
            Pair(Command.valueOf(groups[1]?.value?:"?"), groups[2]?.value?.toLong()?:0L)
        }
    }
    println("There are ${commands.size} commands")

    // Part 1 - follow the commands
    val part1 = commands.fold(SubLocation()) { last, cmd ->
        when (cmd.first) {
            Command.forward -> last.copy(position = last.position + cmd.second)
            Command.up -> last.copy(depth = last.depth - cmd.second)
            Command.down -> last.copy(depth = last.depth + cmd.second)
        }
    }
    println("Part 2 - the product of the submarine depth and position is ${part1.position * part1.depth}")

    // Part 2 - follow the new interpretation of the directions
    val part2 = commands.fold(SubLocation()) { last, cmd ->
        when (cmd.first) {
            Command.forward -> last.copy(position = last.position + cmd.second, depth = last.depth + last.aim * cmd.second)
            Command.up -> last.copy(aim = last.aim - cmd.second)
            Command.down -> last.copy(aim = last.aim + cmd.second)
        }
    }
    println("Part 2 - the product of the submarine depth and position is ${part2.position * part2.depth}")
}

enum class Command { forward, up, down }
data class SubLocation(val position: Long = 0, val depth: Long = 0, val aim: Long = 0)
