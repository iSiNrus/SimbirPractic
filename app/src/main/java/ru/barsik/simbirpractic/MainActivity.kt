package ru.barsik.simbirpractic

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ru.barsik.simbirpractic.databinding.ActivityMainBinding
import ru.barsik.simbirpractic.fragments.CategoriesFragment
import ru.barsik.simbirpractic.fragments.news.NewsFragment
import ru.barsik.simbirpractic.fragments.profile.ProfileFragment
import ru.barsik.simbirpractic.fragments.search.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
           switchFragment(CategoriesFragment())
        }

        binding.bottomNavigation.selectedItemId = R.id.navig_help

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navig_help -> {
                    switchFragment(CategoriesFragment())
                    true
                }
                R.id.navig_profile -> {
                    switchFragment(ProfileFragment())
                    true
                }
                R.id.navig_search -> {
                    switchFragment(SearchFragment())
                    true
                }
                R.id.navig_news -> {
                    switchFragment(NewsFragment())
                    true
                }
                else -> false
            }
        }

    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        showNavigation()
    }
    private fun switchFragment(fm: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fm)
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
    }
}
