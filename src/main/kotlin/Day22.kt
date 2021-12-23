import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    println("2021 Advent of Code day 22")

    // Setup - Load something
    val reactorRegex = """(on|off) x=(-*\d+)..(-*\d+),y=(-*\d+)..(-*\d+),z=(-*\d+)..(-*\d+)""".toRegex()
    val commands = ArrayList<String>()
    val regions = ArrayList<Cuboid>()

    File("day22input").forEachLine { line ->
        reactorRegex.matchEntire(line)?.let { match ->
            commands.add(match.groups[1]!!.value)
            regions.add(Triple(
                IntRange(match.groups[2]!!.value.toInt(), match.groups[3]!!.value.toInt()),
                IntRange(match.groups[4]!!.value.toInt(), match.groups[5]!!.value.toInt()),
                IntRange(match.groups[6]!!.value.toInt(), match.groups[7]!!.value.toInt())
            ))
        }
    }

    // Part 1 - turn on or off the cubes in each range based on the command.
    val enabledCubes = HashSet<Point3D>()
    regions.forEachIndexed { index, range ->
        if (range.toList().all { it.first >= -50 && it.last <= 50 }) {
            range.first.forEach { x ->
                range.second.forEach { y ->
                    range.third.forEach { z ->
                        if (commands[index] == "on")
                            enabledCubes.add(Point3D(x, y, z))
                        else
                            enabledCubes.remove(Point3D(x, y, z))
                    }
                }
            }
        }
    }
    println("Part 1 - There are ${enabledCubes.size} cubes enabled")

    // Part 2 - of course you can't fit it all in a damn set
    val reactor = Reactor()
    regions.forEach { reactor.addRegion(it) }
    commands.forEachIndexed { index, command ->
        if (command == "on")
            reactor.enable(index, true)
        else
            reactor.enable(index, false)
    }
    println("Part 2 - There are ${reactor.enabledCount} cubes enabled")
}

typealias Cuboid = Triple<IntRange, IntRange, IntRange>

class Reactor {
    val rootRegions = ArrayList<ReactorRegion>()

    fun addRegion(range: Cuboid) {
        val newRegion = ReactorRegion(range)
        // Clear tested flag
        rootRegions.forEach { it.clearVisited() }
        // Calculate intersections
        rootRegions.forEach { it.addSubRegions(newRegion) }
        // Add to the root
        rootRegions.add(newRegion)
    }

    fun enable(id: Int, enabled: Boolean) {
        rootRegions[id].enable(enabled)
    }

    val enabledCount: Long
        get() {
            rootRegions.forEach { it.clearVisited() }
            return rootRegions.sumOf { it.enabledCount }
        }
}

class ReactorRegion(val range: Cuboid) {
    val volume = (range.first.count().toLong() * range.second.count().toLong() * range.third.count().toLong())
    private var enabled = false
    private val subRegions = ArrayList<ReactorRegion>()
    private var visited = false  // tracks testing intersections or enabled cubes

    val uniqueVolume: Long
        get() { return volume - subRegions.sumOf { it.uniqueVolume } }

    fun clearVisited() {
        visited = false
        subRegions.forEach { it.clearVisited() }
    }

    fun enable(enabled: Boolean) {
        subRegions.forEach { it.enable(enabled) }
        this.enabled = enabled
    }

    val enabledCount: Long
        get() {
            if (visited) return 0
            visited = true
            return subRegions.sumOf { it.enabledCount } + if(enabled) uniqueVolume else 0
        }

    fun addSubRegions(other: ReactorRegion) {
        // If we tested this region from another parent, it's already complete.
        if (visited) return

        // First let every subregion add any intersections.
        subRegions.forEach { it.addSubRegions(other) }

        // Calculate any overlap and add it as a new subregion to both regions it is part of
        range.intersect(other.range)?.let {
            val newRegion = ReactorRegion(it)
            subRegions.add(newRegion)
            other.subRegions.add(newRegion)
        }
        visited = true
    }
}

// Return the cuboid resulting from intersecting two cuboids.
fun Cuboid.intersect(other: Cuboid): Cuboid? {
    val xRange = IntRange(max(this.first.first, other.first.first), min(this.first.last, other.first.last))
    val yRange = IntRange(max(this.second.first, other.second.first), min(this.second.last, other.second.last))
    val zRange = IntRange(max(this.third.first, other.third.first), min(this.third.last, other.third.last))
    if (xRange.count() > 0 && yRange.count() > 0 && zRange.count() > 0)
        return Cuboid(xRange, yRange, zRange)
    else
        return null
}
