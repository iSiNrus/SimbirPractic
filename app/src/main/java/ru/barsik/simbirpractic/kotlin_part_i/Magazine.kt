package ru.barsik.simbirpractic.kotlin_part_i


/**
 * Создать два класса, реализующих данный интерфейс – Book и Magazine.
 * */
class Magazine(override val price: Float, override val wordCount: Int) : Publication {

    /**
     * Для класса Magazine возвращаем строку “Magazine”
     * */
    override fun getType(): String = "Magazine"

}