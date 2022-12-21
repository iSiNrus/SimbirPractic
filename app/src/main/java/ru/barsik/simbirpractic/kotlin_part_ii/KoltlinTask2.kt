package ru.barsik.simbirpractic.kotlin_part_ii

import java.lang.Thread.sleep


fun main() {

    /**
     * 4. Создать объект класса User, вывести в лог startTime данного юзера,
     * после вызвать Thread.sleep(1000) и повторно вывести в лог startTime.
     * */
    val user = User(1, "John", 145, Type.DEMO)
    println(user.startTime)
    sleep(1000)
    println(user.startTime)

    /**
     * 5. Создать список пользователей, содержащий в себе один объект класса User.
     * Используя функцию apply, добавить ещё несколько объектов класса User в список пользователей.
     * */
    val userList = mutableListOf<User>(user)
    userList.apply {
        add(User(2, "Sara", 34, Type.DEMO))
        add(User(3, "Kate", 25, Type.FULL))
        add(User(5, "Petya", 43, Type.FULL))
    }
    /**
     * 6. Получить список пользователей,
     * у которых имеется полный доступ (поле type имеет значение FULL).
     * */
    userList.filter { it.type == Type.FULL }
    /**
     * 7. Преобразовать список User в список имен пользователей.
     * Получить первый и последний элементы списка и вывести их в лог.
     * */
    val nameList = userList.map { it.name }
    println(nameList[0])
    println(nameList[nameList.size - 1])
    user.checkAge()

    user.auth { updateCache() }

}

/**
 * 8. Создать функцию-расширение класса User,
 * которая проверяет, что юзер старше 18 лет,
 * и в случае успеха выводит в лог, а в случае неуспеха возвращает ошибку.
 * */
fun User.checkAge() {
    if (this.age >= 18) println(age)
    else throw Exception("User младше 18 лет")
}



fun updateCache() {
    println("Update cache")
}

val auth = object : AuthCallback {
    override fun authSuccess() {
        println("Success")
    }

    override fun authFailed() {
        println("Failed")
    }

}