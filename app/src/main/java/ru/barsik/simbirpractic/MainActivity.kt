package ru.barsik.simbirpractic

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.barsik.simbirpractic.databinding.ActivityMainBinding
import ru.barsik.simbirpractic.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.selectedItemId = R.id.navig_profile
        supportFragmentManager.commit {
            add(R.id.fragment_container, ProfileFragment())
        }
    }

    fun hideNavigation(){
        binding.bottomNavigation.visibility = View.GONE
    }

    fun showNavigation(){
        binding.bottomNavigation.visibility = View.VISIBLE
    }
}
