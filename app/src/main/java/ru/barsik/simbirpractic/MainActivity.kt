package ru.barsik.simbirpractic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.barsik.simbirpractic.databinding.ActivityMainBinding
import ru.barsik.simbirpractic.entity.Category
import ru.barsik.simbirpractic.entity.Event
import ru.barsik.simbirpractic.fragments.CategoriesFragment
import ru.barsik.simbirpractic.fragments.news.LoadEventsService
import ru.barsik.simbirpractic.fragments.auth.AuthFragment
import ru.barsik.simbirpractic.fragments.news.NewsFragment
import ru.barsik.simbirpractic.fragments.profile.ProfileFragment
import ru.barsik.simbirpractic.fragments.search.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var categories: ArrayList<Category>? = null
    private var events: ArrayList<Event>? = null
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
           switchFragment(fragmentsMap["Search"] ?:SearchFragment(), addBackStack = false, showBottomNavigation = true)
        }

        binding.bottomNavigation.getOrCreateBadge(R.id.navig_news).number = 10
        binding.bottomNavigation.selectedItemId =
            savedInstanceState?.getInt(OPENED_TAB) ?: R.id.navig_help

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navig_help -> {
                    switchFragment(fragmentsMap["Categories"] ?:CategoriesFragment(), addBackStack = false, showBottomNavigation = true)
                    true
                }
                R.id.navig_profile -> {
                    switchFragment(fragmentsMap["Profile"] ?:ProfileFragment(), addBackStack = false, showBottomNavigation = true)
                    true
                }
                R.id.navig_search -> {
                    switchFragment(fragmentsMap["Search"] ?:SearchFragment(), addBackStack = false, showBottomNavigation = true)
                    true
                }
                R.id.navig_news -> {
                    switchFragment(fragmentsMap["News"] ?:NewsFragment(), addBackStack = false, showBottomNavigation = true)
                    true
                }
                else -> false
            }
        }

    }

    fun getCategories() = categories

    fun setCategories(categories: ArrayList<Category>) {
        this.categories = categories
    }

    fun getEvents() = events

    fun setEvents(events: ArrayList<Event>) {
        this.events = events
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CATEGORIES_LIST, Gson().toJson(categories))
        outState.putString(EVENT_LIST, Gson().toJson(events))
        outState.putInt(OPENED_TAB, binding.bottomNavigation.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val catJsonStr = savedInstanceState?.getString(CATEGORIES_LIST)
        val eventsJsonStr = savedInstanceState?.getString(EVENT_LIST)
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
            Pair("SearchFragment", SearchFragment()),
            Pair("Profile", ProfileFragment()),
            Pair("News", NewsFragment()),
            Pair("Auth", AuthFragment()),
            Pair("Categories", CategoriesFragment())
        )

        private const val CATEGORIES_LIST = "CATEGORIES_LIST"
        private const val EVENT_LIST = "EVENT_LIST"
        private const val OPENED_TAB = "OPENED_TAB"
    }
}
