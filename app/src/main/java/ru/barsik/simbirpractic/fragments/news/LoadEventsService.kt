package ru.barsik.simbirpractic.fragments.news

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import com.google.gson.Gson
import ru.barsik.simbirpractic.dao.EventDAO
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LoadEventsService : Service() {

    private val TAG = "LoadEventsService"

    private var jsonEventsString = ""
    private val handler = Handler(Looper.getMainLooper()) { message ->
        Log.d(TAG, "Handler: $message")
        sendBroadcast(Intent(ACTION_UPDATE).also { it.putExtra(ACTION_UPDATE, jsonEventsString) })
        return@Handler true
    }

    private val task = Runnable {
        TimeUnit.SECONDS.sleep(2)
        EventDAO(applicationContext).getEvents().subscribe{ eventList->
            jsonEventsString = Gson().toJson(eventList)
            handler.sendMessage(Message())
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        Executors.newFixedThreadPool(1).also {
            it.submit(task)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        const val ACTION_UPDATE = "UPDATE"
    }
}