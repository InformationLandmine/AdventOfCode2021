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
    do {
        packets.add(parsePacket(input.drop(packets.sumOf { it.length })))
    } while (input.drop(packets.sumOf { it.length }).filterNot { it == '0' }.isNotEmpty())

    println("Version sum of transmission is ${packets.sumOf { it.version }}")
    println("Value of transmission is ${packets.first().value}")

    println("Done!")
}

class Packet(val version: Int, val type: Int, val length: Int, val value: Long, val subs: List<Packet>)

fun parsePacket(message: String): Packet {
    assert(message.isNotEmpty())

    val subPackets = ArrayList<Packet>()
    var length = 0
    var value = 0L
    var version = message.take(3).toInt(2)
    length += 3
    val type = message.drop(length).take(3).toInt(2)
    length += 3

    when (type) {
        4 -> {      // Literal
            do {
                val group = message.drop(length).take(5)
                length += 5
                value = value.shl(4) + group.drop(1).toLong(2)
            } while (group.first() == '1')
        }
        else -> {
            val lengthType = message.drop(length).take(1).toInt(2)
            length += 1
            when (lengthType) {
                0 -> {
                    val bitLength = message.drop(length).take(15).toInt(2)
                    length += 15
                    var subLength = 0
                    do {
                        subPackets.add(parsePacket(message.drop(length)))
                        subLength += subPackets.last().length
                        length += subPackets.last().length
                        version += subPackets.last().version
                    } while (subLength < bitLength)
                    assert(subLength == bitLength)
                }
                1 -> {
                    val packetCount = message.drop(length).take(11).toInt(2)
                    length += 11
                    do {
                        subPackets.add(parsePacket(message.drop(length)))
                        length += subPackets.last().length
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
    return Packet(version, type, length, value, subPackets)
}
