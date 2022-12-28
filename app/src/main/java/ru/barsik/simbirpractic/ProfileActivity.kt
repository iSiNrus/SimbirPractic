package ru.barsik.simbirpractic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import ru.barsik.simbirpractic.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.selectedItemId = R.id.navig_profile
    }
}