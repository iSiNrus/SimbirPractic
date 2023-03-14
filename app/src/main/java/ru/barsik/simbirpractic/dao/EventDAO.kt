package ru.barsik.simbirpractic.dao

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.barsik.simbirpractic.entity.Event

class EventDAO(private val ctx: Context) {

    fun getEvents(): Observable<List<Event>> {

        val jsonString = ctx.assets.open("events.json")
            .bufferedReader().use { it.readText() }
        val gson = Gson()
        val eventKeyType = object : TypeToken<List<Event>>() {}.type
        val events : List<Event> = gson.fromJson(jsonString, eventKeyType)?: ArrayList()
        return Observable.just(events)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

}