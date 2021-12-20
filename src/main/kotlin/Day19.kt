import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 19")

    // Setup - Load the scanner data
    val point3DRegex = """(-*\d+),(-*\d+),(-*\d+)""".toRegex()
    val scanners = ArrayList<Scanner>()
    val points = ArrayList<Point3D>()
    File("day19input").forEachLine { line ->
        if (line.isEmpty()) {
            scanners.add(Scanner("Scanner ${scanners.size}", points.toList()))
            points.clear()
            return@forEachLine
        }
        point3DRegex.matchEntire(line)?.let {
            points.add(Point3D(it.groups[1]!!.value.toInt(), it.groups[2]!!.value.toInt(), it.groups[3]!!.value.toInt()))
        }
    }

    println(scanners[0].beacons.map { it.y }.sorted())
    println(scanners[17].beacons.map { (it.y + 83) * 1 }.sorted())


    // Part 1 - Orient all the scanners to scanner 0 and find the number of beacons.
    scanners[0].oriented = true
    orientScanner(scanners[0], scanners[17])
//    while (scanners.any { !it.oriented }) {
//        scanners.filter { it.oriented }.forEach { orientedScanner ->
//            scanners.filter { !it.oriented }.forEach { unorientedScanner ->
//                if (!unorientedScanner.nonOverlaps.contains(orientedScanner))
//                    orientScanner(orientedScanner, unorientedScanner)
//            }
//        }
//    }
//    val part1 = scanners.flatMap { it.getAbsoluteBeacons() }.toSet().size
//    println("Part 1 - There are $part1 unique beacons")

    // Part 2
    val part2 = 42
//    println("Part 2 - There are $part2 somethings")
}

data class Point3D (val x: Int, val y:Int, val z:Int) {
    fun flipX() = Point3D(-x, y, -z)
    fun flipY() = Point3D(x, -y, -z)
    fun rotateX() = Point3D(x, z, -y)
    fun rotateY() = Point3D(z, y, -x)
    fun rotateZ() = Point3D(y, -x, z)
    fun rotateXc() = Point3D(x, -z, y)
    fun rotateYc() = Point3D(-z, y, x)
    fun rotateZc() = Point3D(-y, x, z)

    operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)
}

class Scanner(val name: String, var beacons:List<Point3D>) {
    var oriented = false
    var offset = Point3D(0,0,0)
    val nonOverlaps = ArrayList<Scanner>()

    fun flipX() { beacons = beacons.map { it.flipX() } }
    fun flipY() { beacons = beacons.map { it.flipY() } }
    fun rotateX() { beacons = beacons.map { it.rotateX() } }
    fun rotateY() { beacons = beacons.map { it.rotateY() } }
    fun rotateZ() { beacons = beacons.map { it.rotateZ() } }
    fun rotateXc() { beacons = beacons.map { it.rotateXc() } }
    fun rotateYc() { beacons = beacons.map { it.rotateYc() } }
    fun rotateZc() { beacons = beacons.map { it.rotateZc() } }

    fun getAbsoluteBeacons() = beacons.map { it + offset }
}

fun orientScanner(s1: Scanner, s2: Scanner) {
    println("Checking ${s2.name} against ${s1.name}")
    assert(s1.oriented)
    val searchRange = (-2000..2000)

    // Find the matching x-axis and orient to match.
    var match: Pair<Int, Int>
    var localOffset = Point3D(0, 0, 0)
    do {
        match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { (it.x + offset) }).count()) }.maxByOrNull { it.second }!!
        if (match.second >= 12) {
            break
        }
        match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { ((it.x * -1) + offset) }).count()) }.maxByOrNull { it.second }!!
        if (match.second >= 12) {
            s2.flipX(); break
        }
        match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { (it.y + offset) }).count()) }.maxByOrNull { it.second }!!
        if (match.second >= 12) {
            s2.rotateZ(); break
        }
        match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { ((it.y * -1) + offset) }).count()) }.maxByOrNull { it.second }!!
        if (match.second >= 12) {
            s2.rotateZc(); break
        }
        match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { (it.z + offset) }).count()) }.maxByOrNull { it.second }!!
        if (match.second >= 12) {
            s2.rotateY(); break
        }
        match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { ((it.z * -1) + offset) }).count()) }.maxByOrNull { it.second }!!
        if (match.second >= 12) {
            s2.rotateYc(); break
        }
    } while(false)

    if (match.second >= 12) {
        //println("x offset = ${match.first}")
        // Find the matching y-axis and orient to match.
        do {
            match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { (it.y + offset) }).count()) }.maxByOrNull { it.second }!!
            if (match.second >= 12) {
                break
            }
            match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { (it.y + offset) * -1 }).count()) }.maxByOrNull { it.second }!!
            if (match.second >= 12) {
                s2.flipY(); break
            }
            match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { (it.z + offset) }).count()) }.maxByOrNull { it.second }!!
            if (match.second >= 12) {
                s2.rotateX(); break
            }
            match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { (it.z + offset) * -1 }).count()) }.maxByOrNull { it.second }!!
            if (match.second >= 12) {
                s2.rotateXc(); break
            }
        } while(false)

        if (match.second >= 12) {

            //println("y offset = ${match.first}")

            // The z-axis should match now that we've solved the other two, so just find the offset
            match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.x }.intersect(s2.beacons.map { (it.x + offset) }).count()) }.maxByOrNull { it.second }!!
            assert (match.second >= 12)
            //println("x offset = ${match.first}")
            localOffset = localOffset.copy(x = match.first)
            match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.y }.intersect(s2.beacons.map { (it.y + offset) }).count()) }.maxByOrNull { it.second }!!
            //println("y offset = ${match.first}")
            assert (match.second >= 12)
            localOffset = localOffset.copy(y = match.first)
            match = searchRange.map { offset -> Pair(offset, s1.beacons.map { it.z }.intersect(s2.beacons.map { (it.z + offset) }).count()) }.maxByOrNull { it.second }!!
            assert (match.second >= 12)
            localOffset = localOffset.copy(z = match.first)

            s2.offset = s1.offset + localOffset
            s2.oriented = true
            println("${s2.name} oriented by ${s1.name}")
            println(localOffset)
            //println(localOffset)
            //println(s2.offset)
            //println("")
        } else {
            println("${s2.name} matched only one axis of ${s1.name}")
            s1.nonOverlaps.add(s2)
            s2.nonOverlaps.add(s1)
        }
    } else {
        s1.nonOverlaps.add(s2)
        s2.nonOverlaps.add(s1)
    }
}