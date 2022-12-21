package ru.barsik.simbirpractic.kotlin_part_ii

import java.time.LocalTime

/**
 * 3. Реализовать класс данных User с полями id, name, age и type.
 * У класса User создать ленивое свойство startTime, в котором получаем текущее время.
 * */
data class User(val id: Int, val name: String, val age: Int, val type: Type) {
    val startTime: LocalTime by lazy { LocalTime.now() }

    /**
     * 10. Реализовать inline функцию auth, принимающую в качестве параметра функцию updateCache.
     * Функция updateCache должна выводить в лог информацию об обновлении кэша.
     * */
    inline fun auth(cache: () -> Unit) {
        /**
         * 11. Внутри функции auth вызвать метод коллбека authSuccess и переданный updateCache,
         * если проверка возраста пользователя произошла без ошибки.
         * В случае получения ошибки вызвать authFailed.
         * */
        try {
            this.checkAge()
            auth.authSuccess()
            cache()
        } catch (e : Exception){
            auth.authFailed()
        }
    }

    /**
     * 13. Реализовать метод doAction, принимающий экземпляр класса Action.
     * В зависимости от переданного действия выводить в лог текст, к примеру “Auth started”.
     * Для действия Login вызывать метод auth.
     * */
    fun doAction(act : Action){
        when (act) {
            is Registration -> println("Registration")
            is Login -> {
                println("Login")
                this.auth { updateCache() }
            }
            is Logout -> println("Logout")
        }
    }
}