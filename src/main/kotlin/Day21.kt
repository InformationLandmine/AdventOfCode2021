import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 21")

    // Setup - Load something
    val p1Start = 7
    val p2Start = 2
    //val p1Start = 4
    //val p2Start = 8

    // Part 1 - play a game with deterministic dice
    val game = DiracDiceBoard(10)
    game.p1Pos = p1Start
    game.p2Pos = p2Start

    while (game.gameWinner == Player.none) {
        game.play()
    }
    val part1 = game.die.rollCount * if (game.gameWinner == Player.player1) game.p2Score else game.p1Score
    println("rollcount = ${game.die.rollCount}")
    println("gamewinner = ${game.gameWinner}")
    println("p1Score = ${game.p1Score}")
    println("p2Score = ${game.p2Score}")
    println("Part 1 - $part1")

    // Part 2 - play real Dirac Dice
    val gameState = DiracDiceState(
        p1Start,
        p2Start,
        0,
        0,
        TurnState.start,
        0,
        Player.player1
    )
    val winCounts = playDiracDice(gameState)

    println("Part 2 ")
}
val scoreMap = HashMap<DiracDiceState, Pair<Long, Long>>()
var p1wins = 0L
var p2Wins = 0L

class DeterministicDie(val sides: Int) {
    var currentVal = 0
    var rollCount = 0
    fun roll(): Int {
        if (++currentVal > sides) currentVal = 1
        rollCount++
        return currentVal
    }
}

class DiracDiceBoard(val spaces: Int) {
    var p1Pos = 0
    var p2Pos = 0
    var p1Score = 0
    var p2Score = 0
    var gameWinner = Player.none
    var turn = Player.player1
    val die = DeterministicDie(100)

    fun moveP1(count: Int) {
        repeat (count) { if (++p1Pos > spaces) p1Pos = 1 }
        //position += dice_total % 10, and if position > 10, position -=10
        //p1Pos += (count % spaces)
        //if (p1Pos > count) p1Pos -= spaces
        p1Score += p1Pos
        if (p1Score >= 1000)
            gameWinner = Player.player1
    }

    fun moveP2(count: Int) {
        repeat (count) { if (++p2Pos > spaces) p2Pos = 1 }
//        p2Pos += (count % spaces)
  //      if (p2Pos > count) p2Pos -= spaces
        p2Score += p2Pos
        if (p2Score >= 1000)
            gameWinner = Player.player2
    }

    fun play() {
        val toMove = die.roll() + die.roll() + die.roll()
        when (turn) {
            Player.player1 -> { moveP1(toMove); turn = Player.player2 }
            Player.player2 -> { moveP2(toMove); turn = Player.player1 }
        }
    }
}

enum class Player { none, player1, player2 }
enum class TurnState { start, roll1, roll2, roll3, move, score, end }

data class DiracDiceState(
    var p1Pos: Int,
    var p2Pos: Int,
    var p1Score: Int,
    var p2Score: Int,
    var turnState: TurnState,
    var rollTotal: Int,
    var turn: Player,
)

fun playDiracDice(state: DiracDiceState): Pair<Long, Long> {
    while (true) {
        when (state.turnState) {
            TurnState.start -> {
                state.rollTotal = 0
                state.turnState = TurnState.roll1
            }
            TurnState.roll1 -> {
                if (scoreMap.containsKey(state)) {
                    return(scoreMap.getOrDefault(state, Pair(0L, 0L)))
                } else {
                    state.turnState = TurnState.roll2
                    val result1 = playDiracDice(state.copy(rollTotal = state.rollTotal + 1))
                    val result2 = playDiracDice(state.copy(rollTotal = state.rollTotal + 2))
                    val result3 = playDiracDice(state.copy(rollTotal = state.rollTotal + 3))
                    val winTotals = Pair(
                        result1.first + result2.first + result3.first,
                        result1.second + result2.second + result3.second
                    )
                    scoreMap[state.copy(turnState = TurnState.roll1)] = winTotals
                    return winTotals
                }
            }
            TurnState.roll2 -> {
                if (scoreMap.containsKey(state)) {
                    return(scoreMap.getOrDefault(state, Pair(0L, 0L)))
                } else {
                    state.turnState = TurnState.roll3
                    val result1 = playDiracDice(state.copy(rollTotal = state.rollTotal + 1))
                    val result2 = playDiracDice(state.copy(rollTotal = state.rollTotal + 2))
                    val result3 = playDiracDice(state.copy(rollTotal = state.rollTotal + 3))
                    val winTotals = Pair(
                        result1.first + result2.first + result3.first,
                        result1.second + result2.second + result3.second
                    )
                    scoreMap[state.copy(turnState = TurnState.roll2)] = winTotals
                    return winTotals
                }
            }
            TurnState.roll3 -> {
                if (scoreMap.containsKey(state)) {
                    return(scoreMap.getOrDefault(state, Pair(0L, 0L)))
                } else {
                    state.turnState = TurnState.move
                    val result1 = playDiracDice(state.copy(rollTotal = state.rollTotal + 1))
                    val result2 = playDiracDice(state.copy(rollTotal = state.rollTotal + 2))
                    val result3 = playDiracDice(state.copy(rollTotal = state.rollTotal + 3))
                    val winTotals = Pair(
                        result1.first + result2.first + result3.first,
                        result1.second + result2.second + result3.second
                    )
                    scoreMap[state.copy(turnState = TurnState.roll3)] = winTotals
                    return winTotals
                }

            }
            TurnState.move -> {
                when (state.turn) {
                    Player.player1 -> repeat (state.rollTotal) { if (++state.p1Pos > 10) state.p1Pos = 1 }
                    Player.player2 -> repeat (state.rollTotal) { if (++state.p2Pos > 10) state.p2Pos = 1 }
                }
                state.turnState = TurnState.score
            }
            TurnState.score -> {
                when (state.turn) {
                    Player.player1 -> {
                        state.p1Score += state.p1Pos
                        if (state.p1Score >= 21) {
                            p1wins++;
                            return Pair(1L, 0L)
                        }
                    }
                    Player.player2 -> {
                        state.p2Score += state.p2Pos
                        if (state.p2Score >= 21) {
                            p2Wins++;
                            return Pair(0L, 1L)
                        }
                    }
                }
                state.turnState = TurnState.end
            }
            TurnState.end -> {
                state.turn = if (state.turn == Player.player1) Player.player2 else Player.player1
                state.turnState = TurnState.start
            }
        }
    }
}

