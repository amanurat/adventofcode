fun minimumOf(vararg numbers: Int): Int {
    var minimum: Int = numbers[0]
    for (number in numbers) {
        minimum = Math.min(number, minimum)
    }
    return minimum
}
