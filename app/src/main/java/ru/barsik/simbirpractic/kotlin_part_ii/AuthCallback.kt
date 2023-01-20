package ru.barsik.simbirpractic.kotlin_part_ii

/**
 * 9. Создать интерфейс AuthCallback с методами authSuccess, authFailed
 * и реализовать анонимный объект данного интерфейса.
 * В методах необходимо вывести в лог информацию о статусе авторизации.
 * */
interface AuthCallback {

    fun authSuccess()

    fun authFailed()
}

