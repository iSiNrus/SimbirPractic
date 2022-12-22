package ru.barsik.simbirpractic.kotlin_part_i


/**
 * Создать два объекта класса Book и один объект Magazine.
 * Вывести в лог для каждого объекта тип, количество строк и цену в евро в отформатированном виде.
 * У класса Book переопределить метод equals и произвести сравнение сначала по ссылке,
 * затем используя метод equals. Результаты сравнений вывести в лог.
 * */
fun main() {

    val b1 = Book(20.0f, 320_240)
    val b2 = Book(18.34f, 6_578)

    val m1 = Magazine(8.55f, 80_000)

    println("${b1.getType()} ${b1.wordCount} ${b1.price}")
    println("${b2.getType()} ${b2.wordCount} ${b2.price}")
    println("${m1.getType()} ${m1.wordCount} ${m1.price}")

    println(b1 == b2)
    println(b1.equals(b2))

    /**
     * Создать две переменных класса Book, в которых могут находиться null значения.
     * Присвоить одной null, а второй любое notnull значение.
     * Используя функцию let, попробуйте вызвать метод buy с каждой из переменных.
     * */
    val b3: Book? = null
    val b4: Book? = Book(25f, 54000)

    b3?.let { buy(b3) }
    b4?.let { buy(b4) }

    /**
     * Создать переменную sum и присвоить ей лямбда-выражение,
     * которое будет складывать два переданных ей числа и выводить результат в лог.
     * Вызвать данное лямбда-выражение с произвольными параметрами.
     * */
    val sum: (Int, Int) -> Unit = { x: Int, y: Int -> println(x + y) }

    sum(5, 3)
}

/**
 * Создать метод buy, который в качестве параметра принимает Publication (notnull - значения)
 * и выводит в лог “The purchase is complete. The purchase amount was [цена издания]”.
 * */
fun buy(publication: Publication) {
    println("The purchase is complete. The purchase amount was ${publication.price}")
}