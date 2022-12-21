package ru.barsik.simbirpractic.kotlin_part_i

/**
 * Создать два класса, реализующих данный интерфейс – Book и Magazine.
 * */
class Book(override val price: Float, override val wordCount: Int) : Publication {

    /**
     * В методе getType для класса Book возвращаем строку с зависимости от количества слов
     * @return “Flash Fiction”, если количество слов не превышает 1000
     * @return “Short Story”, если количество слов не превышает 7,500
     * @return “Novel”, если количество слов превышает 7,500
     * */
    override fun getType(): String {
        return when(wordCount){
            in 1..1000 -> "Flash Fiction"
            in 1001..7500 -> "Short Story"
            else -> "Novel"
        }
    }

    override fun equals(other: Any?): Boolean {
        val b = other as Book
        return b.price == this.price && b.wordCount==this.wordCount
    }

}