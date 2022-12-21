package ru.barsik.simbirpractic.kotlin_part_i

/**
 * Необходимо создать интерфейс Publication,
 * у которого должно быть свойства –
 * price и wordCount, а также метод getType, возвращающий строку.
 * */
interface Publication {

    val price: Float
    val wordCount: Int

    fun getType(): String

}