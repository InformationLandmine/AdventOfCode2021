import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 13")

    // Setup - Load the lists of points and folds.
    val points = ArrayList<Point>()
    val folds = ArrayList<Pair<Char, Int>>()
    File("day13input").forEachLine {
        val coords = it.split(',')
        if (coords.size == 2)
            points.add(Point(coords[0].toInt(), coords[1].toInt()))
        else {
            val fold = it.split('=')
            if (fold.size == 2) {
                folds.add(Pair(fold[0].last(), fold[1].toInt()))
            }
        }
    }

    var foldedPoints = points.toSet()
    folds.forEachIndexed { i, fold ->
        when (fold.first) {
            'y' -> {
                foldedPoints = foldedPoints.map {
                    if (it.y > fold.second)
                        Point(it.x, fold.second - (it.y - fold.second))
                    else
                        it
                }.toSet()
            }
            'x' -> {
                foldedPoints = foldedPoints.map {
                    if (it.x > fold.second)
                        Point(fold.second - (it.x - fold.second), it.y)
                    else
                        it
                }.toSet()
            }
        }
        if (i == 0) println("After the first fold there are ${foldedPoints.size} visible points")
    }

    val grid = Array(foldedPoints.maxOf { it.y } + 1) { Array(foldedPoints.maxOf { it.x } + 1) { '.' } }
    foldedPoints.forEach { grid[it.y][it.x] = '#' }
    grid.forEach { println(it.joinToString(" ")) }
}
