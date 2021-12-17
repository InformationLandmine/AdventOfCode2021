import kotlin.system.measureTimeMillis

//val X_RANGE = (20..30)
//val Y_RANGE = (-10..-5)
val X_RANGE = (269..292)
val Y_RANGE = (-68..-44)

fun main(args: Array<String>) {
    println("2021 Advent of Code day 17")

    val successfulProbes = ArrayList<Probe>()
    val timeMs = measureTimeMillis {
        for (x_vel in 1..X_RANGE.last) {
            for (y_vel in Y_RANGE.first..100) {
                val probe = Probe(MutablePoint(x_vel, y_vel))
                while (!(probe.outOfRange || probe.inRange)) {
                    probe.step()
                    if (probe.inRange) successfulProbes.add(probe)
                }
            }
        }
    }
    println("Part 1 - the maximum height reached by any successful probe is ${successfulProbes.maxOf { it.maxY }}")
    println("Part 2 - the number of successful probes is ${successfulProbes.count()}")
    println(timeMs)
}

class MutablePoint(var x: Int, var y: Int)

class Probe(val vel: MutablePoint) {
    val pos = MutablePoint(0, 0)
    var maxY = 0

    val inRange get() = (pos.x in X_RANGE && pos.y in Y_RANGE)
    val outOfRange get() = (pos.x > X_RANGE.last || pos.y < Y_RANGE.first) || (vel.x == 0 && pos.x < X_RANGE.first)

    fun step() {
        pos.x += vel.x
        pos.y += vel.y
        vel.x = if (vel.x < 0) vel.x + 1 else if (vel.x > 0) vel.x - 1 else 0
        vel.y -= 1
        if (pos.y > maxY) maxY = pos.y
    }
}
