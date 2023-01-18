package ru.barsik.simbirpractic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.barsik.simbirpractic.databinding.ActivityProfileBinding
import ru.barsik.simbirpractic.fragments.ProfileAvatarDialog

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.selectedItemId = R.id.navig_profile

        binding.ivAvatar.setOnClickListener {
            ProfileAvatarDialog().show(supportFragmentManager, "avatar")
        }
    }
}
