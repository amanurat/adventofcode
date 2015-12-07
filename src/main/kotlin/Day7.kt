import extensions.lines
import rx.Observable
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    day7()
}

fun day7() {
    println("Day 7")

    val file = File("input/day7.txt")
    println("1: ${partOne(file)}")
    println("2: ${partTwo(file)}")
}

private fun partOne(input: File): Int {
    return input.lines()
            .toAssignment()
            .collect(
                    { HashMap<String, Expression>() },
                    {
                        m, a ->
                        m.put(a.target, a.expression)
                    }
            )
            .map { it["a"]!!.execute(it) }
            .toBlocking()
            .first()
}

private fun partTwo(input: File): Int {
    return input.lines()
            .toAssignment()
            .collect(
                    { HashMap<String, Expression>() },
                    {
                        m, a ->
                        m.put(a.target, a.expression)
                    }
            )
            .doOnNext { it["b"] = NullaryExpression(partOne(input)) }
            .map { it["a"]!!.execute(it) }
            .toBlocking()
            .first()
}

private fun Observable<String>.toAssignment(): Observable<Assignment> {
    return map { it.split(' ') }
            .map {
                Assignment(
                        expression = toExpression(it.subList(0, it.size - 2)),
                        target = it.last()
                )
            }
}

private fun toExpression(input: List<String>): Expression {
    if (input.size == 1) {
        return toExpression(input[0])
    } else if (input.size == 2) {
        return UnaryExpression(
                operator = input[0].toUnaryOperator(),
                expr = toExpression(input[1])
        )
    } else {
        return BinaryExpression(
                left = toExpression(input[0]),
                operator = input[1].toBinaryOperator(),
                right = toExpression(input[2])
        )
    }
}

private fun toExpression(input: String): Expression {
    if (input.isInt()) {
        return NullaryExpression(input.toInt())
    } else {
        return Identifier(input)
    }
}

private fun String.isInt(): Boolean {
    return get(0).isDigit() && if (length > 1 ) substring(1).isInt() else true
}

private fun String.toUnaryOperator(): UnaryOperator =
        when (this) {
            "NOT" -> UnaryOperator.NOT
            else -> throw Error(this)
        }

private fun String.toBinaryOperator(): BinaryOperator =
        when (this) {
            "AND" -> BinaryOperator.AND
            "OR" -> BinaryOperator.OR
            "LSHIFT" -> BinaryOperator.LSHIFT
            "RSHIFT" -> BinaryOperator.RSHIFT
            else -> throw Error(this)
        }

private class Assignment(val expression: Expression, val target: String) {

    override fun toString(): String {
        return "$expression -> $target"
    }
}

private interface Expression {
    fun execute(values: HashMap<String, Expression>): Int
}

private class Identifier(val identifier: String) : Expression {

    private var result: Int? = null

    override fun execute(values: HashMap<String, Expression>): Int {
        return result ?:
                values[identifier]!!.execute(values).let {
                    result = it
                    it
                }
    }
}

private class NullaryExpression(val value: Int) : Expression {

    override fun execute(values: HashMap<String, Expression>): Int {
        return value
    }
}

private class UnaryExpression(val operator: UnaryOperator, var expr: Expression) : Expression {

    override fun execute(values: HashMap<String, Expression>): Int {
        return operator.execute(expr.execute(values))
    }
}

private class BinaryExpression(var left: Expression, val operator: BinaryOperator, var right: Expression) : Expression {

    override fun execute(values: HashMap<String, Expression>): Int {
        val r = right.execute(values)
        val l = left.execute(values)

        return operator.execute(l, r)
    }
}

private fun getValue(expr: String, values: Map<String, Int>): Int? {
    if (expr.isInt()) {
        return expr.toInt()
    }

    return values[expr]
}

private enum class UnaryOperator {
    NOT {
        override fun execute(i: Int): Int {
            return i.inv() and 0xFFFF
        }
    };

    abstract fun execute(i: Int): Int
}

private enum class BinaryOperator {
    AND {
        override fun execute(left: Int, right: Int): Int {
            return left and right
        }
    },
    OR {
        override fun execute(left: Int, right: Int): Int {
            return left or right
        }
    },
    LSHIFT {
        override fun execute(left: Int, right: Int): Int {
            return left shl right
        }
    },
    RSHIFT {
        override fun execute(left: Int, right: Int): Int {
            return left shr right
        }
    };

    abstract fun execute(left: Int, right: Int): Int
}
