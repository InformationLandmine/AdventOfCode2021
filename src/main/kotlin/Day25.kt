import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 25")

    // Setup - Load something
    val input: SeaMap = File("day25input").readLines().map { row ->
        row.map { SeaMapLocation(it, false) }
    }

    // Part 1 - iterate the map until nothing moves
    var part1 = 0
    var numMoves: Int
    do {
        numMoves = 0
        numMoves += input.markEastMovers()
        input.moveEast()
        numMoves += input.markSouthMovers()
        input.moveSouth()
        part1++
    } while (numMoves > 0)

    println("Part 1 - Nothing moves after $part1 turns")
    println("Part 2 is a freebie! Merry Christmas!")
}
typealias SeaMap = List<List<SeaMapLocation>>
class SeaMapLocation(var contents: Char, var willMove: Boolean)

fun SeaMap.markEastMovers(): Int {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { index, location ->
            if (index == row.size -1) {
                if (location.contents == '>' && this[rowIndex][0].contents == '.') { location.willMove = true }
            } else if (location.contents == '>' && this[rowIndex][index+1].contents == '.') { location.willMove = true }
        }
    }
    return this.sumOf { it.count { it.willMove } }
}

fun SeaMap.moveEast() {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { index, location ->
            if (index == row.size -1) {
                if (location.willMove) {
                    location.contents = '.'
                    this[rowIndex][0].contents = '>'
                    location.willMove = false
                }
            } else if (location.willMove) {
                location.contents = '.'
                this[rowIndex][index+1].contents = '>'
                location.willMove = false
            }
        }
    }
}

fun SeaMap.markSouthMovers(): Int {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { index, location ->
            if (rowIndex == this.size -1) {
                if (location.contents == 'v' && this[0][index].contents == '.') { location.willMove = true }
            } else if (location.contents == 'v' && this[rowIndex + 1][index].contents == '.') { location.willMove = true }
        }
    }
    return this.sumOf { it.count { it.willMove } }
}

fun SeaMap.moveSouth() {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { index, location ->
            if (rowIndex == this.size -1) {
                if (location.willMove) {
                    location.contents = '.'
                    this[0][index].contents = 'v'
                    location.willMove = false
                }
            } else if (location.willMove) {
                location.contents = '.'
                this[rowIndex + 1][index].contents = 'v'
                location.willMove = false
            }
        }
    }
}
