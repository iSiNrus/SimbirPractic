package ru.barsik.simbirpractic.dao

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.barsik.simbirpractic.entity.Event

class EventDAO(private val ctx: Context) {

    private var events = ArrayList<Event>()

    fun getEvents(): List<Event> {
        if (events.isEmpty()) {
            val jsonString = ctx.assets.open("events.json")
                    .bufferedReader().use { it.readText() }
            val gson = Gson()
            val eventKeyType = object : TypeToken<List<Event>>() {}.type
            events = gson.fromJson(jsonString, eventKeyType)
        }
        return events
    }

}