package ru.barsik.simbirpractic.dao

import android.content.Context
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.barsik.simbirpractic.entity.Category
import java.util.concurrent.TimeUnit

class CategoryDAO(private val ctx: Context) {

    private val TAG = "CategoryDAO"
    private var categories: ArrayList<Category>? = null

    fun getCategories(): List<Category> {
        if (categories == null) {

            val jsonString = ctx.assets.open("categories.json")
                .bufferedReader().use { it.readText() }

            val gson = Gson()
            val catKeyType = object : TypeToken<List<Category>>() {}.type
            categories = gson.fromJson(jsonString, catKeyType)
        }
        return categories!!
    }

    fun getCategoryFromServer(): Observable<ArrayList<Category>> {
        return Observable.create { emitter ->
            val ref = FirebaseDatabase.getInstance().reference.child("categories")
            ref.get().addOnSuccessListener { data ->
                categories = ArrayList()
                for (child in data.children) {
                    child.getValue(Category::class.java)?.let { categories!!.add(it) }
                }
                Log.d(TAG, "onResume: ${categories!!.size}")
                emitter.onNext(categories!!)
                emitter.onComplete()
            }.addOnCanceledListener {
                Log.d(TAG, "onCanceled")
                emitter.onComplete()
            }.addOnCompleteListener {
                Log.d(TAG, "onCompleted")
                emitter.onComplete()
            }
        }
            .timeout(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }


    fun getCategoryByTitle(mTitle: String): Category {
        if (categories == null) getCategories()
        return categories?.find { it ->
            it.title == mTitle
        } ?: Category(3, "dsf", "")
    }
}