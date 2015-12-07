data class Dimensions(val length: Int, val width: Int, val height: Int) {

    fun area() = 2 * length * width + 2 * width * height + 2 * height * length

    fun volume() = length * width * height
}