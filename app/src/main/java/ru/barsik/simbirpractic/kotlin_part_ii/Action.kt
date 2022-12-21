package ru.barsik.simbirpractic.kotlin_part_ii

/**
 * 12.Реализовать изолированный класс Action и его наследников – Registration, Login и Logout.
 * Login должен принимать в качестве параметра экземпляр класса User.
 * */
sealed class Action()

class Registration : Action()
class Login(val user: User) : Action()
class Logout : Action()
