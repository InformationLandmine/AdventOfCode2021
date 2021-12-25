import java.io.File

fun main(args: Array<String>) {
    println("2021 Advent of Code day 24")

    // Setup - Load something
    val program = ArrayList<Instruction>()
    val ALU_REGEX = """(inp|add|mul|div|mod|eql) ([wxyz]) ?([wxyz]-*\d+)?""".toRegex()
    File("day24input").forEachLine { line ->
        ALU_REGEX.matchEntire(line)?.let { match ->
            val hasOp2 = match.groups.count { it != null } > 3
            val operator = Operator.values().firstOrNull { it.toString() == match.groups[1]!!.value }!!
            val op1reg = Register.values().first { it.toString() == match.groups[2]!!.value }
            val op2reg = if (hasOp2) Register.values().firstOrNull { it.toString() == match.groups[3]!!.value } else null
            val op2val = if (hasOp2) match.groups[3]!!.value.toIntOrNull() else null
            program.add(Instruction(operator, op1reg, op2reg, op2val))
        }
    }

    // Running the ALU doesn't do anything to help! Instead, it was more of a logic problem to read the program and
    // figure out how to get the z register to be 0 at the end.
    // From the below hand-execution of the program, the following rules were determined:
    // d must == c + 8
    // e must == b - 3
    // i must == h + 2
    // j must == g - 1
    // k must == f - 4
    // m must == l - 5
    // n must == a - 7

    // Therefore, the largest model number accepted is:
    // 99196997985942
    // And the smallest is:
    // 84191521311611
}

fun runProgram(instructions: List<Instruction>, input: ArrayDeque<Int>): IntArray {
    val registers = IntArray(4)
    instructions.forEach { inst ->
        when (inst.operator) {
            Operator.inp -> registers[inst.op1reg.ordinal] = input.removeFirst()
            Operator.add -> {
                inst.op2reg?.let { registers[inst.op1reg.ordinal] += registers[it.ordinal] }
                inst.op2val?.let { registers[inst.op1reg.ordinal] += it }
            }
            Operator.mul -> {
                inst.op2reg?.let { registers[inst.op1reg.ordinal] *= registers[it.ordinal] }
                inst.op2val?.let { registers[inst.op1reg.ordinal] *= it }
            }
            Operator.div -> {
                inst.op2reg?.let { registers[inst.op1reg.ordinal] /= registers[it.ordinal] }
                inst.op2val?.let { registers[inst.op1reg.ordinal] /= it }
            }
            Operator.mod -> {
                inst.op2reg?.let { registers[inst.op1reg.ordinal] %= registers[it.ordinal] }
                inst.op2val?.let { registers[inst.op1reg.ordinal] %= it }
            }
            Operator.eql -> {
                inst.op2reg?.let {
                    registers[inst.op1reg.ordinal] =
                        if (registers[inst.op1reg.ordinal] == registers[it.ordinal]) 1 else 0
                }
                inst.op2val?.let { registers[inst.op1reg.ordinal] = if (registers[inst.op1reg.ordinal] == it) 1 else 0 }
            }
        }
    }
    //println ("Output: w = ${registers[0]}, x = ${registers[1]}, y = ${registers[2]}, z = ${registers[3]}")
    return registers
}

class Instruction(val operator: Operator, val op1reg: Register, val op2reg: Register?, val op2val: Int?)
enum class Register { w, x, y, z }
enum class Operator { inp, add, mul, div, mod, eql }

//inp w           w = a
//mul x 0
//add x z
//mod x 26
//div z 1
//add x 12        x = 12
//eql x w         x = 0        // a cannot be 12
//eql x 0         x = 1
//mul y 0
//add y 25        y = 25
//mul y x
//add y 1         y = 26
//mul z y
//mul y 0         y = 0
//add y w         y = a
//add y 1         y = a + 1
//mul y x
//add z y         z = a + 1
//
////////////// added 'a' term ////////////////
//
//
//inp w           w = b
//mul x 0
//add x z         x = a + 1
//mod x 26
//div z 1
//add x 12        x = a + 13
//eql x w         x = 0             // b == a + 13? (impossible in this step)
//eql x 0         x = 1
//mul y 0
//add y 25        y = 25
//mul y x         y = 25
//add y 1         y = 26
//mul z y         z = 26(a + 1)
//mul y 0
//add y w         y = b
//add y 1         y = b + 1
//mul y x
//add z y         z = 26(a + 1) + (b + 1)
//
////////////// added 'b' term ////////////////
//
//
//inp w           w = c
//mul x 0
//add x z
//mod x 26
//div z 1
//add x 15
//eql x w         x = 0     // impossible to match as w will be 1-9
//eql x 0         x = 1
//mul y 0
//add y 25        y = 25
//mul y x         y = 25
//add y 1         y = 26
//mul z y         z = 26(26(a + 1) + (b + 1))
//mul y 0
//add y w
//add y 16
//mul y x         y = c + 16
//add z y         z = 26(26(a + 1) + (b + 1)) + (c + 16)
//
////////////// added 'c' term ////////////////
//
//
//inp w           w = d
//mul x 0
//add x z
//mod x 26        x = c + 16
//div z 26        z = 26(a + 1) + (b + 1)
//add x -8        x = c + 8
//eql x w         1 if d == c + 8
//eql x 0         1 if d != c + 8
//mul y 0
//add y 25
//mul y x         0 if d == c + 8
//add y 1         1 or 26
//mul z y
//mul y 0
//add y w
//add y 5         y = d + 5
//mul y x
//add z y         should not change z as long as d == c + 8
//
//////////////// d must == c + 8 ///////////////
//////////////// a and b remain ////////////////
//
//inp w           w = e
//mul x 0
//add x z         x = 26(a + 1) + (b + 1)
//mod x 26        x = b + 1
//div z 26        z = a + 1
//add x -4        x = b - 3
//eql x w         1 if e == b - 3
//eql x 0         1 if e != b - 3
//mul y 0
//add y 25
//mul y x         0 if e == b - 3
//add y 1         1 or 26
//mul z y
//mul y 0
//add y w
//add y 9         y = e + 9
//mul y x
//add z y         should not change z as long as e == b - 3
//
//////////////// e must == b - 3 ///////////////
//////////////// a remains /////////////////////
//
//
//inp w           w = f
//mul x 0
//add x z         x = a + 1
//mod x 26        x = a + 1
//div z 1
//add x 15        x = a + 16
//eql x w         x = 0        // impossible as f is 1-9
//eql x 0         x = 1
//mul y 0
//add y 25
//mul y x
//add y 1         y = 26
//mul z y         z = 26(a + 1)
//mul y 0
//add y w
//add y 3         y = f + 3
//mul y x
//add z y         z = 26(a + 1) + (f + 3)
//
////////////// added 'f' term ////////////////
//
//
//inp w           w = g
//mul x 0
//add x z         x = 26(a + 1) + (f + 3)
//mod x 26        x = f + 3
//div z 1
//add x 14        x = f + 17
//eql x w         x = 0        // impossible as g is 1-9
//eql x 0         x = 1
//mul y 0
//add y 25
//mul y x
//add y 1
//mul z y         z = 26(26(a + 1) + (f + 3))
//mul y 0
//add y w
//add y 2         y = g + 2
//mul y x
//add z y         z = 26(26(a + 1) + (f + 3)) + (g + 2)
//
////////////// added 'g' term ////////////////
//
//
//inp w           w = h
//mul x 0
//add x z         x = 26(26(a + 1) + (f + 3)) + (g + 2)
//mod x 26        x = g + 2
//div z 1
//add x 14        x = g + 16
//eql x w         x = 0        // impossible as h is 1-9
//eql x 0         x = 1
//mul y 0
//add y 25
//mul y x
//add y 1
//mul z y         z = 26(26(26(a + 1) + (f + 3)) + (g + 2))
//mul y 0
//add y w
//add y 15        y = h + 15
//mul y x
//add z y         z = 26(26(26(a + 1) + (f + 3)) + (g + 2)) + h + 15
//
////////////// added 'h' term ////////////////
//
//
//inp w           w = i
//mul x 0
//add x z
//mod x 26        x = h + 15
//div z 26        z = 26(26(a + 1) + (f + 3)) + (g + 2)
//add x -13       x = h + 2
//eql x w         1 if i == h + 2
//eql x 0         1 if i != h + 2
//mul y 0
//add y 25
//mul y x         0 if i == h + 2
//add y 1         y = 1
//mul z y         z = 26(26(a + 1) + (f + 3)) + (g + 2)
//mul y 0
//add y w
//add y 5         y = i + 5
//mul y x
//add z y         should not change z as long as i == h + 2
//
//////////////// i must == h + 2 ///////////////
//////////////// a, f, g remain ////////////////
//
//
//inp w           w = j
//mul x 0
//add x z
//mod x 26        x = g + 2
//div z 26        z = 26(a + 1) + (f + 3)
//add x -3        x = g - 1
//eql x w         1 if j == g - 1
//eql x 0         1 if j != g = 1
//mul y 0
//add y 25
//mul y x         0 if j == g - 1
//add y 1         y = 1
//mul z y         z = 26(a + 1) + (f + 3)
//mul y 0
//add y w
//add y 11        y = j + 11
//mul y x
//add z y         should not change z as long as j == g - 1
//
//////////////// j must == g - 1 ///////////////
//////////////// a, f remain ///////////////////
//
//
//inp w           w = k
//mul x 0
//add x z         x = 26(a + 1) + (f + 3)
//mod x 26        x = f + 3
//div z 26        z = a + 1
//add x -7        x = f - 4
//eql x w         1 if k == f - 4
//eql x 0         1 if k != f - 4
//mul y 0
//add y 25
//mul y x         0 if k == f - 4
//add y 1         y = 1
//mul z y         z = a + 1
//mul y 0
//add y w
//add y 7         y = k + 7
//mul y x
//add z y         should not change z as long as k == f - 4
//
//////////////// k must == f - 4 ///////////////
//////////////// a remains /////////////////////
//
//
//inp w           w = l
//mul x 0
//add x z         x = a + 1
//mod x 26        x = a + 1
//div z 1         z = a + 1
//add x 10        x = a + 10
//eql x w         x = 0    // impossible because l is 1-9
//eql x 0         x = 1
//mul y 0
//add y 25
//mul y x
//add y 1         y = 26
//mul z y         z = 26(a + 1)
//mul y 0
//add y w
//add y 1         y = l + 1
//mul y x
//add z y         z = 26(a + 1) + l + 1
//
////////////// added 'l' term ////////////////
//
//
//inp w           w = m
//mul x 0
//add x z         x = 26(a + 1) + l + 1
//mod x 26        x = l + 1
//div z 26        z = a + 1
//add x -6        x = l - 5
//eql x w         1 if m == l - 5
//eql x 0         1 if m != l - 5
//mul y 0
//add y 25
//mul y x         0 if m == l - 5
//add y 1         y = 1
//mul z y         z = a + 1
//mul y 0
//add y w
//add y 10        y = m + 10
//mul y x
//add z y         should not change z as long as m == l - 5
//
//////////////// m must == l - 5 ///////////////
//////////////// a remains /////////////////////
//
//
//inp w           w = n
//mul x 0
//add x z
//mod x 26        x = a + 1
//div z 26        z = 0
//add x -8        x = a - 7
//eql x w         1 if n == a - 7
//eql x 0         1 if n != a - 7
//mul y 0
//add y 25
//mul y x
//add y 1         y = 1
//mul z y         z = 0
//mul y 0
//add y w
//add y 3         y = n + 3
//mul y x
//add z y         z = 0 as long as n == a - 7
//
//////////////// n must == a - 7 ///////////////
//////////////// nothing remains ///////////////
