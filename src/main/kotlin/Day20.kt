import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 20")

    // Setup - Load something
    var image = Image()
    val input = File("day20input").readLines()
    val algorithm = input[0]
    input.drop(2).forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            if (c == '#') image.add(Point(col, row))
        }
    }

    val origXRange = (image.minOf { it.x })..(image.maxOf { it.x })
    val origYRange = (image.minOf { it.y })..(image.maxOf { it.y })

    // Start with a grid large enough so border "artifacts" can't encroach on the actual image.
    val xRange = (image.minOf { it.x - 102 })..(image.maxOf { it.x + 102 })
    val yRange = (image.minOf { it.y - 102 })..(image.maxOf { it.y + 102 })

    // Parts 1 and 2 - iterate the image 50 times.
    repeat(50) { count ->
        val newImage = Image()
        xRange.forEach { y ->
            yRange.forEach { x ->
                if (algorithm[image.getAlgorithmLoc(Point(x, y))] == '#') newImage.add(Point(x, y))
            }
        }
        image = newImage
        if (count == 1) {
            val part1 = image.count {
                it.x in (origXRange.first - (count + 1))..(origXRange.last + (count + 1)) &&
                it.y in (origYRange.first - (count + 1))..(origYRange.last + (count + 1))
            }
            println("Part 1 - There are $part1 lit pixels")
        }
    }
    val part2 = image.count {
        it.x in (origXRange.first-50)..(origXRange.last + 50) &&
        it.y in (origYRange.first-50)..(origYRange.last + 50)
    }
    println("Part 2 - There are $part2 lit pixels")

}

typealias Image = HashSet<Point>

fun Image.getAlgorithmLoc(pixel: Point): Int {
    var result = ""
    result += if (this.contains(Point(pixel.x - 1, pixel.y - 1))) "1" else "0"
    result += if (this.contains(Point(pixel.x, pixel.y - 1))) "1" else "0"
    result += if (this.contains(Point(pixel.x + 1, pixel.y - 1))) "1" else "0"
    result += if (this.contains(Point(pixel.x - 1, pixel.y))) "1" else "0"
    result += if (this.contains(Point(pixel.x, pixel.y))) "1" else "0"
    result += if (this.contains(Point(pixel.x + 1, pixel.y))) "1" else "0"
    result += if (this.contains(Point(pixel.x - 1, pixel.y + 1))) "1" else "0"
    result += if (this.contains(Point(pixel.x, pixel.y + 1))) "1" else "0"
    result += if (this.contains(Point(pixel.x + 1, pixel.y + 1))) "1" else "0"
    return result.toInt(2)
}