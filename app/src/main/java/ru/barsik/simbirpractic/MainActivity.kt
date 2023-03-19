package ru.barsik.simbirpractic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.barsik.simbirpractic.databinding.ActivityMainBinding
import ru.barsik.simbirpractic.entity.Category
import ru.barsik.simbirpractic.entity.Event
import ru.barsik.simbirpractic.fragments.CategoriesFragment
import ru.barsik.simbirpractic.fragments.auth.AuthFragment
import ru.barsik.simbirpractic.fragments.news.LoadEventsService
import ru.barsik.simbirpractic.fragments.news.NewsFragment
import ru.barsik.simbirpractic.fragments.profile.ProfileFragment
import ru.barsik.simbirpractic.fragments.search.SearchFragment
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var categories: ArrayList<Category>? = null
    private var events: ArrayList<Event>? = null
    private lateinit var readEventsIds: HashSet<String>
    private var showNewsIds = HashSet<String>()
    val newsUpdaterFlow: MutableStateFlow<Int?> = MutableStateFlow(null)

    fun setShowNewsIds(ids: HashSet<String>){
        showNewsIds = ids
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            switchFragment(
                fragmentsMap["News"] ?: SearchFragment(),
                addBackStack = false,
                showBottomNavigation = true
            )
        }
        val prefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE)
        readEventsIds = prefs.getStringSet(READ_EVENTS, HashSet<String>()) as HashSet<String>
        binding.bottomNavigation.selectedItemId =
            savedInstanceState?.getInt(OPENED_TAB) ?: R.id.navig_news

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navig_help -> {
                    switchFragment(
                        fragmentsMap["Categories"] ?: CategoriesFragment(),
                        addBackStack = false,
                        showBottomNavigation = true
                    )
                    true
                }
                R.id.navig_profile -> {
                    switchFragment(
                        fragmentsMap["Profile"] ?: ProfileFragment(),
                        addBackStack = false,
                        showBottomNavigation = true
                    )
                    true
                }
                R.id.navig_search -> {
                    switchFragment(
                        fragmentsMap["Search"] ?: SearchFragment(),
                        addBackStack = false,
                        showBottomNavigation = true
                    )
                    true
                }
                R.id.navig_news -> {
                    switchFragment(
                        fragmentsMap["News"] ?: NewsFragment(),
                        addBackStack = false,
                        showBottomNavigation = true
                    )
                    true
                }
                else -> false
            }
        }

        newsUpdaterFlow.onEach {
            if (it != null) {
            withContext(Dispatchers.Default) {
                    Log.d(TAG, "onEach: $it")
                    readEvent(it)
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.Default))
    }

    fun updateReadEventsView() {
        binding.bottomNavigation.getOrCreateBadge(R.id.navig_news).number =
            showNewsIds.count { x -> !readEventsIds.contains(x) }
    }

    private suspend fun readEvent(id: Int) {
        readEventsIds.add(id.toString())
        updateReadEventsView()
    }

    fun getCategories() = categories

    fun setCategories(categories: ArrayList<Category>) {
        this.categories = categories
    }

    fun getEvents() = events

    fun setEvents(events: ArrayList<Event>) {
        this.events = events
        updateReadEventsView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CATEGORIES_LIST, Gson().toJson(categories))
        outState.putString(EVENT_LIST, Gson().toJson(events))
        outState.putInt(OPENED_TAB, binding.bottomNavigation.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val catJsonStr = savedInstanceState.getString(CATEGORIES_LIST)
        val eventsJsonStr = savedInstanceState.getString(EVENT_LIST)
        if (eventsJsonStr != null)
            events = Gson().fromJson(eventsJsonStr, object : TypeToken<List<Event>>() {}.type)
        if (catJsonStr != null)
            categories = Gson().fromJson(catJsonStr, object : TypeToken<List<Category>>() {}.type)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        showNavigation()
    }

    fun switchFragment(fm: Fragment, addBackStack: Boolean, showBottomNavigation: Boolean) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fm)
            if (addBackStack) addToBackStack(null)
            if (showBottomNavigation) showNavigation()
            else hideNavigation()
        }
    }

    fun addFragment(fm: Fragment) {
        supportFragmentManager.commit {
            add(R.id.fragment_container, fm)
        }
    }

    fun hideNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun showNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    override fun onPause() {
        val prefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE)
        prefs.edit().putStringSet(READ_EVENTS, readEventsIds).apply()
        super.onPause()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            applicationContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                supportFragmentManager.commit {
                    add(R.id.fragment_container, CategoriesFragment())
                }
            } else return
        }
    }

    fun startServiceForEvents() {
        this.startService(Intent(this, LoadEventsService::class.java))
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
        val fragmentsMap = hashMapOf(
            Pair("Search", SearchFragment()),
            Pair("Profile", ProfileFragment()),
            Pair("News", NewsFragment()),
            Pair("Auth", AuthFragment()),
            Pair("Categories", CategoriesFragment())
        )

        private const val CATEGORIES_LIST = "CATEGORIES_LIST"
        private const val EVENT_LIST = "EVENT_LIST"
        private const val OPENED_TAB = "OPENED_TAB"
        const val APP_PREFS = "App_Prefs"
        const val READ_EVENTS = "Read_events"
    }
}
