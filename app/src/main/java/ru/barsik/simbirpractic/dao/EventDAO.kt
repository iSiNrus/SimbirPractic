package ru.barsik.simbirpractic.dao

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.barsik.simbirpractic.entity.Event
import java.util.concurrent.TimeUnit

class EventDAO(private val ctx: Context) {

    private val TAG = "EventDAO"
    private var _events: ArrayList<Event>? = null
    fun getEvents(): Observable<ArrayList<Event>> {
        return Observable.create { emitter1 ->
            if (_events != null)  _events
            else {
                val t = getEventsFromServer().subscribe({
                    Log.d(TAG, "From Server")
                    Toast.makeText(ctx, "From Server", Toast.LENGTH_SHORT).show()
                    emitter1.onNext(it)
                    emitter1.onComplete()
                }, {
                    val t2 = getEventsFromFile().subscribe {
                        Log.d(TAG, "From File")
                        Toast.makeText(ctx, "From File", Toast.LENGTH_SHORT).show()
                        emitter1.onNext(it)
                        emitter1.onComplete()
                    }
                })
            }
        }
    }

    private fun getEventsFromFile(): Observable<ArrayList<Event>> {
        val jsonString = ctx.assets.open("events.json")
            .bufferedReader().use { it.readText() }
        val gson = Gson()
        val eventKeyType = object : TypeToken<List<Event>>() {}.type
        val events: ArrayList<Event> = gson.fromJson(jsonString, eventKeyType) ?: ArrayList()
        _events = events
        return Observable.just(_events!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
    private fun getEventsFromServer(): Observable<ArrayList<Event>> {
            return Observable.create { emitter ->
                val ref = FirebaseDatabase.getInstance().reference.child("events")
                ref.get().addOnSuccessListener { data ->
                    Log.d(TAG, "onSuccess")
                    val events = ArrayList<Event>()
                    for (child in data.children)
                        child.getValue(Event::class.java)?.let { events.add(it) }
                    emitter.onNext(events)
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

}