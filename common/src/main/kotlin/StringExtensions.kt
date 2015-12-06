fun String.chars(): Iterable<Char> {
    return object : Iterable<Char> {
        override fun iterator(): Iterator<Char> {
            return object : Iterator<Char> {

                private var index = 0

                override fun next(): Char {
                    return get(index++)
                }

                override fun hasNext(): Boolean {
                    return index < length
                }
            }
        }
    }
}