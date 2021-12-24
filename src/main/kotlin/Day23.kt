import java.lang.Exception
import java.util.HashMap

fun main(args: Array<String>) {
    println("2021 Advent of Code day 23")

    // Setup - Define the map of the amphipod habitat
    val locations = ArrayList<Location>()
    locations.add(Location(0, LocationType.Hallway, Occupant.None, Occupant.None))
    locations.add(Location(1, LocationType.Hallway, Occupant.None, Occupant.None))
    locations.add(Location(2, LocationType.Doorway, Occupant.None, Occupant.None))
    locations.add(Location(3, LocationType.Hallway, Occupant.None, Occupant.None))
    locations.add(Location(4, LocationType.Doorway, Occupant.None, Occupant.None))
    locations.add(Location(5, LocationType.Hallway, Occupant.None, Occupant.None))
    locations.add(Location(6, LocationType.Doorway, Occupant.None, Occupant.None))
    locations.add(Location(7, LocationType.Hallway, Occupant.None, Occupant.None))
    locations.add(Location(8, LocationType.Doorway, Occupant.None, Occupant.None))
    locations.add(Location(9, LocationType.Hallway, Occupant.None, Occupant.None))
    locations.add(Location(10, LocationType.Hallway, Occupant.None, Occupant.None))

//    locations.add(Location(11, LocationType.Room, Occupant.A, Occupant.B))
//    locations.add(Location(12, LocationType.Room, Occupant.A, Occupant.A))
//    locations.add(Location(13, LocationType.Room, Occupant.B, Occupant.C))
//    locations.add(Location(14, LocationType.Room, Occupant.B, Occupant.D))
//    locations.add(Location(15, LocationType.Room, Occupant.C, Occupant.B))
//    locations.add(Location(16, LocationType.Room, Occupant.C, Occupant.C))
//    locations.add(Location(17, LocationType.Room, Occupant.D, Occupant.D))
//    locations.add(Location(18, LocationType.Room, Occupant.D, Occupant.A))
    // Answer: 12521

    locations.add(Location(11, LocationType.Room, Occupant.A, Occupant.D))
    locations.add(Location(12, LocationType.Room, Occupant.A, Occupant.C))
    locations.add(Location(13, LocationType.Room, Occupant.B, Occupant.C))
    locations.add(Location(14, LocationType.Room, Occupant.B, Occupant.D))
    locations.add(Location(15, LocationType.Room, Occupant.C, Occupant.A))
    locations.add(Location(16, LocationType.Room, Occupant.C, Occupant.A))
    locations.add(Location(17, LocationType.Room, Occupant.D, Occupant.B))
    locations.add(Location(18, LocationType.Room, Occupant.D, Occupant.B))


    // Part 1
    val part1 = solveGame(locations, 0)
    println("Part 1 - The cheapest cost is ${part1}")

    // Part 2
    val part2 = 42
    println("Part 2 - There are $part2 somethings")
}

val moves = mapOf(
    // 11 to hallway
    Pair(Pair(11, 0), listOf(2, 1, 0)),
    Pair(Pair(11, 1), listOf(2, 1)),
    Pair(Pair(11, 3), listOf(2, 3)),
    Pair(Pair(11, 5), listOf(2, 3, 4, 5)),
    Pair(Pair(11, 7), listOf(2, 3, 4, 5, 6, 7)),
    Pair(Pair(11, 9), listOf(2, 3, 4, 5, 6, 7, 8, 9)),
    Pair(Pair(11, 10), listOf(2, 3, 4, 5, 6, 7, 8, 9, 10)),

    // 12 to hallway
    Pair(Pair(12, 0), listOf(11, 2, 1, 0)),
    Pair(Pair(12, 1), listOf(11, 2, 1)),
    Pair(Pair(12, 3), listOf(11, 2, 3)),
    Pair(Pair(12, 5), listOf(11, 2, 3, 4, 5)),
    Pair(Pair(12, 7), listOf(11, 2, 3, 4, 5, 6, 7)),
    Pair(Pair(12, 9), listOf(11, 2, 3, 4, 5, 6, 7, 8, 9)),
    Pair(Pair(12, 10), listOf(11, 2, 3, 4, 5, 6, 7, 8, 9, 10)),

    // 13 to hallway
    Pair(Pair(13, 0), listOf(4, 3, 2, 1, 0)),
    Pair(Pair(13, 1), listOf(4, 3, 2, 1)),
    Pair(Pair(13, 3), listOf(4, 3)),
    Pair(Pair(13, 5), listOf(4, 5)),
    Pair(Pair(13, 7), listOf(4, 5, 6, 7)),
    Pair(Pair(13, 9), listOf(4, 5, 6, 7, 8, 9)),
    Pair(Pair(13, 10), listOf(4, 5, 6, 7, 8, 9, 10)),

    // 14 to hallway
    Pair(Pair(14, 0), listOf(13, 4, 3, 2, 1, 0)),
    Pair(Pair(14, 1), listOf(13, 4, 3, 2, 1)),
    Pair(Pair(14, 3), listOf(13, 4, 3)),
    Pair(Pair(14, 5), listOf(13, 4, 5)),
    Pair(Pair(14, 7), listOf(13, 4, 5, 6, 7)),
    Pair(Pair(14, 9), listOf(13, 4, 5, 6, 7, 8, 9)),
    Pair(Pair(14, 10), listOf(13, 4, 5, 6, 7, 8, 9, 10)),

    // 15 to hallway
    Pair(Pair(15, 0), listOf(6, 5, 4, 3, 2, 1, 0)),
    Pair(Pair(15, 1), listOf(6, 5, 4, 3, 2, 1)),
    Pair(Pair(15, 3), listOf(6, 5, 4, 3)),
    Pair(Pair(15, 5), listOf(6, 5)),
    Pair(Pair(15, 7), listOf(6, 7)),
    Pair(Pair(15, 9), listOf(6, 7, 8, 9)),
    Pair(Pair(15, 10), listOf(6, 7, 8, 9, 10)),

    // 16 to hallway
    Pair(Pair(16, 0), listOf(15, 6, 5, 4, 3, 2, 1, 0)),
    Pair(Pair(16, 1), listOf(15, 6, 5, 4, 3, 2, 1)),
    Pair(Pair(16, 3), listOf(15, 6, 5, 4, 3)),
    Pair(Pair(16, 5), listOf(15, 6, 5)),
    Pair(Pair(16, 7), listOf(15, 6, 7)),
    Pair(Pair(16, 9), listOf(15, 6, 7, 8, 9)),
    Pair(Pair(16, 10), listOf(15, 6, 7, 8, 9, 10)),

    // 17 to hallway
    Pair(Pair(17, 0), listOf(8, 7, 6, 5, 4, 3, 2, 1, 0)),
    Pair(Pair(17, 1), listOf(8, 7, 6, 5, 4, 3, 2, 1)),
    Pair(Pair(17, 3), listOf(8, 7, 6, 5, 4, 3)),
    Pair(Pair(17, 5), listOf(8, 7, 6, 5)),
    Pair(Pair(17, 7), listOf(8, 7)),
    Pair(Pair(17, 9), listOf(8, 9)),
    Pair(Pair(17, 10), listOf(8, 9, 10)),

    // 18 to hallway
    Pair(Pair(18, 0), listOf(17, 8, 7, 6, 5, 4, 3, 2, 1, 0)),
    Pair(Pair(18, 1), listOf(17, 8, 7, 6, 5, 4, 3, 2, 1)),
    Pair(Pair(18, 3), listOf(17, 8, 7, 6, 5, 4, 3)),
    Pair(Pair(18, 5), listOf(17, 8, 7, 6, 5)),
    Pair(Pair(18, 7), listOf(17, 8, 7)),
    Pair(Pair(18, 9), listOf(17, 8, 9)),
    Pair(Pair(18, 10), listOf(17, 8, 9, 10)),

    // 0 to rooms
    Pair(Pair(0, 11), listOf(1, 2, 11)),
    Pair(Pair(0, 12), listOf(1, 2, 11, 12)),
    Pair(Pair(0, 13), listOf(1, 2, 3, 4, 13)),
    Pair(Pair(0, 14), listOf(1, 2, 3, 4, 13, 14)),
    Pair(Pair(0, 15), listOf(1, 2, 3, 4, 5, 6, 15)),
    Pair(Pair(0, 16), listOf(1, 2, 3, 4, 5, 6, 15, 16)),
    Pair(Pair(0, 17), listOf(1, 2, 3, 4, 5, 6, 7, 8, 17)),
    Pair(Pair(0, 18), listOf(1, 2, 3, 4, 5, 6, 7, 8, 17, 18)),

    // 1 to rooms
    Pair(Pair(1, 11), listOf(2, 11)),
    Pair(Pair(1, 12), listOf(2, 11, 12)),
    Pair(Pair(1, 13), listOf(2, 3, 4, 13)),
    Pair(Pair(1, 14), listOf(2, 3, 4, 13, 14)),
    Pair(Pair(1, 15), listOf(2, 3, 4, 5, 6, 15)),
    Pair(Pair(1, 16), listOf(2, 3, 4, 5, 6, 15, 16)),
    Pair(Pair(1, 17), listOf(2, 3, 4, 5, 6, 7, 8, 17)),
    Pair(Pair(1, 18), listOf(2, 3, 4, 5, 6, 7, 8, 17, 18)),

    // 3 to rooms
    Pair(Pair(3, 11), listOf(2, 11)),
    Pair(Pair(3, 12), listOf(2, 11, 12)),
    Pair(Pair(3, 13), listOf(4, 13)),
    Pair(Pair(3, 14), listOf(4, 13, 14)),
    Pair(Pair(3, 15), listOf(4, 5, 6, 15)),
    Pair(Pair(3, 16), listOf(4, 5, 6, 15, 16)),
    Pair(Pair(3, 17), listOf(4, 5, 6, 7, 8, 17)),
    Pair(Pair(3, 18), listOf(4, 5, 6, 7, 8, 17, 18)),

    // 5 to rooms
    Pair(Pair(5, 11), listOf(4, 3, 2, 11)),
    Pair(Pair(5, 12), listOf(4, 3, 2, 11, 12)),
    Pair(Pair(5, 13), listOf(4, 13)),
    Pair(Pair(5, 14), listOf(4, 13, 14)),
    Pair(Pair(5, 15), listOf(6, 15)),
    Pair(Pair(5, 16), listOf(6, 15, 16)),
    Pair(Pair(5, 17), listOf(6, 7, 8, 17)),
    Pair(Pair(5, 18), listOf(6, 7, 8, 17, 18)),

    // 7 to rooms
    Pair(Pair(7, 11), listOf(6, 5, 4, 3, 2, 11)),
    Pair(Pair(7, 12), listOf(6, 5, 4, 3, 2, 11, 12)),
    Pair(Pair(7, 13), listOf(6, 5, 4, 13)),
    Pair(Pair(7, 14), listOf(6, 5, 4, 13, 14)),
    Pair(Pair(7, 15), listOf(6, 15)),
    Pair(Pair(7, 16), listOf(6, 15, 16)),
    Pair(Pair(7, 17), listOf(8, 17)),
    Pair(Pair(7, 18), listOf(8, 17, 18)),

    // 9 to rooms
    Pair(Pair(9, 11), listOf(8, 7, 6, 5, 4, 3, 2, 11)),
    Pair(Pair(9, 12), listOf(8, 7, 6, 5, 4, 3, 2, 11, 12)),
    Pair(Pair(9, 13), listOf(8, 7, 6, 5, 4, 13)),
    Pair(Pair(9, 14), listOf(8, 7, 6, 5, 4, 13, 14)),
    Pair(Pair(9, 15), listOf(8, 7, 6, 15)),
    Pair(Pair(9, 16), listOf(8, 7, 6, 15, 16)),
    Pair(Pair(9, 17), listOf(8, 17)),
    Pair(Pair(9, 18), listOf(8, 17, 18)),

    // 10 to rooms
    Pair(Pair(10, 11), listOf(9, 8, 7, 6, 5, 4, 3, 2, 11)),
    Pair(Pair(10, 12), listOf(9, 8, 7, 6, 5, 4, 3, 2, 11, 12)),
    Pair(Pair(10, 13), listOf(9, 8, 7, 6, 5, 4, 13)),
    Pair(Pair(10, 14), listOf(9, 8, 7, 6, 5, 4, 13, 14)),
    Pair(Pair(10, 15), listOf(9, 8, 7, 6, 15)),
    Pair(Pair(10, 16), listOf(9, 8, 7, 6, 15, 16)),
    Pair(Pair(10, 17), listOf(9, 8, 17)),
    Pair(Pair(10, 18), listOf(9, 8, 17, 18)),
)

data class Location(val id: Int, val type: LocationType, val owner: Occupant, var occupant: Occupant)
enum class LocationType { Hallway, Doorway, Room }
enum class Occupant { None, A, B, C, D }

var bestCost = Int.MAX_VALUE
val stateMap = HashMap<List<Location>, Int>()

fun solveGame(locations: List<Location>, costSoFar: Int): Int {
    // If we have exceeded the best cost found so far, bail.
    if (costSoFar >= bestCost)
        return Int.MAX_VALUE

    // If solved, return cost.
    if (isGameSolved(locations)) {
        if (costSoFar < bestCost)
            bestCost = costSoFar
        return costSoFar
    }

    // If we have seen this state before, just lookup the lowest cost from here.
    stateMap[locations]?.let {
        if (it != Int.MAX_VALUE)
            return it + costSoFar
    }
//    if (stateMap.containsKey(locations)){
//        return stateMap[locations]!! + costSoFar
//    }

    // Get valid moves and their costs.
    val validMoves = getValidMoves(locations)

    // If we are at a dead end, return.
    if (validMoves.isEmpty()) {
        return Int.MAX_VALUE
    }
    // Otherwise, make each valid move and continue.
    var lowestCost = Int.MAX_VALUE
    var additionalCost = Int.MAX_VALUE
    validMoves.forEach { move ->
        val newLocations = locations.map { it.copy() }
        try {
            assert(newLocations[move.second].occupant == Occupant.None)
            assert(newLocations[move.first].occupant != Occupant.None)
        }
        catch (e: AssertionError) {
            printMap(newLocations)
            println("Illegal move")
        }
        newLocations[move.second].occupant = newLocations[move.first].occupant
        newLocations[move.first].occupant = Occupant.None

        // If we have seen this state before, return the cost, otherwise solve it and add the cost
        val thisCost = solveGame(newLocations, costSoFar + move.third)
        if (thisCost < lowestCost) {
            lowestCost = thisCost
            additionalCost = thisCost - costSoFar
        }
    }

    // Add the best solution from this state to the map.
    if (lowestCost == Int.MAX_VALUE)
        stateMap[locations] = Int.MAX_VALUE
    else
        stateMap[locations] = additionalCost

    return lowestCost
}

fun getValidMoves(locations: List<Location>): List<Triple<Int, Int, Int>> {
    val result = ArrayList<Triple<Int, Int, Int>>()

    val startingLocations = locations.filter {
        it.occupant != Occupant.None &&
        !(it.id % 2 == 0 && it.occupant == it.owner) &&
        !(it.id % 2 == 1 && it.occupant == it.owner && (locations[it.id + 1].occupant == locations[it.id + 1].owner))
    }

    startingLocations.forEach { start ->
        // Narrow down the possible destinations.
        var destinations = locations.filter { end ->
            end.occupant == Occupant.None &&
            end.type != LocationType.Doorway &&
            (if (end.type == LocationType.Room) end.owner == start.occupant else true) &&
            (if (start.type == LocationType.Hallway) end.type == LocationType.Room else true) &&
            (if (start.type == LocationType.Room) end.type == LocationType.Hallway else true)
        }
        // If we are moving into a room, it must be empty and the deeper location, or the shallow location only if the correct roommate is present
        destinations = destinations.filter { end ->
            end.type == LocationType.Hallway ||
            (end.id % 2 == 0 && start.type == LocationType.Hallway) ||
            (end.id % 2 == 1 && locations[end.id + 1].occupant == locations[end.id + 1].owner)
        }
        // Get non-blocked destinations.
        try {
            destinations = destinations.filter { moves[Pair(start.id, it.id)]!!.all { locations[it].occupant == Occupant.None } }
        }
        catch (e: Exception) {
            println("shit")
        }
        destinations.forEach { end ->
            val moveCost = moves[Pair(start.id, end.id)]!!.size * cost[start.occupant.ordinal]
            result.add(Triple(start.id, end.id, moveCost))
            try {
                assert(start.occupant != Occupant.None)
                assert(end.occupant == Occupant.None)
            }
            catch (e: AssertionError) {
                printMap(locations)
                println("Illegal move")
            }
        }
    }
    return result
}

fun isGameSolved(location: List<Location>): Boolean {
    return (
        location[11].occupant == Occupant.A &&
        location[12].occupant == Occupant.A &&
        location[13].occupant == Occupant.B &&
        location[14].occupant == Occupant.B &&
        location[15].occupant == Occupant.C &&
        location[16].occupant == Occupant.C &&
        location[17].occupant == Occupant.D &&
        location[18].occupant == Occupant.D
    )
}

fun printMap(loc: List<Location>) {
    println("#############")
    println("#${charForLoc(loc[0])}${charForLoc(loc[1])}${charForLoc(loc[2])}${charForLoc(loc[3])}${charForLoc(loc[4])}${charForLoc(loc[5])}${charForLoc(loc[6])}${charForLoc(loc[7])}${charForLoc(loc[8])}${charForLoc(loc[9])}${charForLoc(loc[10])}#")
    println("###${charForLoc(loc[11])}#${charForLoc(loc[13])}#${charForLoc(loc[15])}#${charForLoc(loc[17])}###")
    println("  #${charForLoc(loc[12])}#${charForLoc(loc[14])}#${charForLoc(loc[16])}#${charForLoc(loc[18])}#  ")
    println("  #########")
}

fun charForLoc(loc: Location): Char {
    return when (loc.occupant) {
        Occupant.None -> '.'
        Occupant.A -> 'A'
        Occupant.B -> 'B'
        Occupant.C -> 'C'
        Occupant.D -> 'D'
    }
}

//fun allOccupantsPresent(loc: List<Location>): Boolean {
//    return loc.count { it.occupant == Occupant.A } == 2 &&
//            loc.count { it.occupant == Occupant.B } == 2 &&
//            loc.count { it.occupant == Occupant.C } == 2 &&
//            loc.count { it.occupant == Occupant.D } == 2
//}

val cost = arrayOf(Int.MAX_VALUE, 1, 10, 100, 1000)
