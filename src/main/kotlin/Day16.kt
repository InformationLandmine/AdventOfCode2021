import java.io.ByteArrayInputStream
import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 16")

    // Setup - Load the navigation subsystem
    val input = File("day16input").readLines().first()
    //val input = "38006F45291200" // example 1 operator
    //val input = "EE00D40C823060" // example 2 operator
    //val input = "8A004A801A8002F478" // final example 1; sum = 16
    //val input = "620080001611562C8802118E34" // final example 2; sum = 12
    //val input = "C0015000016115A2E0802F182340" // final example 2; sum = 23
    //val input = "A0016C880162017C3686B18A3D4780" // final example 2; sum = 31
        .map { String.format("%4s", it.toString().toInt(16).toString(2)).replace(" ".toRegex(), "0") }
        .joinToString("")

    val packets = ArrayList<Packet>()
    val message = input.byteInputStream()

    do {
        packets.add(parsePacket(message))
    } while (input.drop(packets.sumOf { it.length }).filterNot { it == '0' }.isNotEmpty())

    println("Version sum of transmission is ${packets.sumOf { it.version }}")
    println("Value of transmission is ${packets.first().value}")
}

class Packet(val version: Int, val length: Int, val value: Long)

fun parsePacket(msg: ByteArrayInputStream): Packet {
    val initial = msg.available()
    var value = 0L
    var version = msg.readNBytes(3).decodeToString().toInt(2)
    val type = msg.readNBytes(3).decodeToString().toInt(2)

    when (type) {
        4 -> {      // Literal
            do {
                val group = msg.readNBytes(5).decodeToString()
                value = value.shl(4) + group.drop(1).toLong(2)
            } while (group.first() == '1')
        }
        else -> {   // Operator
            val subPackets = ArrayList<Packet>()
            val lengthType = msg.readNBytes(1).decodeToString().toInt(2)
            when (lengthType) {
                0 -> {
                    val bitLength = msg.readNBytes(15).decodeToString().toInt(2)
                    var subLength = 0
                    do {
                        subPackets.add(parsePacket(msg))
                        subLength += subPackets.last().length
                        version += subPackets.last().version
                    } while (subLength < bitLength)
                }
                1 -> {
                    val packetCount = msg.readNBytes(11).decodeToString().toInt(2)
                    do {
                        subPackets.add(parsePacket(msg))
                        version += subPackets.last().version
                    } while (subPackets.size < packetCount)
                }
            }
            value = when (type) {
                0 -> subPackets.sumOf { it.value }
                1 -> subPackets.fold(1) { acc, it -> acc * it.value }
                2 -> subPackets.minOf { it.value }
                3 -> subPackets.maxOf { it.value }
                5 -> if (subPackets[0].value > subPackets[1].value)  1 else 0
                6 -> if (subPackets[0].value < subPackets[1].value)  1 else 0
                7 -> if (subPackets[0].value == subPackets[1].value)  1 else 0
                else -> 0
            }
        }
    }
    return Packet(version, initial - msg.available(), value)
}
