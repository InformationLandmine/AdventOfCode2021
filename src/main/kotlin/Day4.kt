import java.io.File
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main(args: Array<String>) {
    println("2021 Advent of Code day 4")

    // Setup - Read the bingo draws and cards.
    val input = File("day4input").readLines()
    val draws = ArrayDeque(input[0].split(',').map { it.toInt() })
    val cards = input.drop(1).windowed(6, 6).map {
        it.joinToString(" ").replace("  ", " ").trim().split(' ').map { Spot(it.toInt(), false) }
    }.map { BingoCard(it) }
    println("There are ${cards.size} bingo cards and ${draws.size} draws")

    var currentDraw = 0
    var lastWinningCard = cards.first()
    while (!cards.any { it.isWinner() }) {
        currentDraw = draws.removeFirst()
        cards.forEach { it.markSpot(currentDraw) }
    }
    println ("The score of the first winning card is ${cards.first { it.isWinner() }.calculateScore(currentDraw)}")
    while (cards.count { !it.isWinner() } > 0)
    {
        currentDraw = draws.removeFirst()
        cards.forEach { it.markSpot(currentDraw) }
        if (cards.count { !it.isWinner() } == 1) lastWinningCard = cards.first { !it.isWinner() }
    }
    println ("The score of the last winning card is ${lastWinningCard.calculateScore(currentDraw)}")
}

class Spot(var num: Int, var marked: Boolean)

class BingoCard(private val spots: List<Spot>) {
    private val size: Int = sqrt(spots.size.toDouble()).roundToInt()
    private val rows = spots.windowed(size, size)
    private val cols = spots.windowed(size * size - size + 1).map { it.slice(0 until (size * size) step size) }
    private val groups = listOf(rows, cols).flatten()

    fun markSpot(num: Int) = spots.firstOrNull { it.num == num }?.let { it.marked = true }
    fun isWinner() = groups.any { group -> group.all { spot -> spot.marked } }
    fun calculateScore(calledNum: Int) = spots.filter { !it.marked }.sumOf { it.num } * calledNum

    override fun toString() = rows.map { row -> row.fold("") { line, spot -> line + if (spot.marked) "[${spot.num}]\t" else " ${spot.num} \t" } + '\n' }.joinToString("") { it }
}