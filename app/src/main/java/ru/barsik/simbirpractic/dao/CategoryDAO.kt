package ru.barsik.simbirpractic.dao

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.barsik.simbirpractic.entity.Category

class CategoryDAO(private val ctx: Context) {

    private var categories = ArrayList<Category>()

    fun getCategories(): List<Category> {
        if (categories.isEmpty()) {

            val jsonString = ctx.assets.open("categories.json")
                .bufferedReader().use { it.readText() }

            val gson = Gson()
            val catKeyType = object : TypeToken<List<Category>>() {}.type
            categories = gson.fromJson(jsonString, catKeyType)
        }
        return categories
    }
}