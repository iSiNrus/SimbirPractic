package ru.barsik.simbirpractic.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import ru.barsik.simbirpractic.MainActivity
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.databinding.FragmentEventInfoBinding
import ru.barsik.simbirpractic.entity.Event
import ru.barsik.simbirpractic.fragments.news.FilterNewsFragment

class EventInfoFragment(private val event: Event) : Fragment() {

    private lateinit var binding : FragmentEventInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            (requireActivity() as MainActivity).showNavigation()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.share_event_menu_item -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
            true
        }

        binding.tvToolbarTitle.text = event.title
        binding.tvEventTitle.text = event.title

        binding.tvLocation.text = event.location
        binding.ivImgs1.setImageBitmap(BitmapFactory.decodeStream(
            resources.assets.open(event.title_img_path)))
        binding.ivImgs2.setImageBitmap(BitmapFactory.decodeStream(
            resources.assets.open(event.title_img_path)))
        binding.ivImgs3.setImageBitmap(BitmapFactory.decodeStream(
            resources.assets.open(event.title_img_path)))

        binding.tvDescription.text = event.description

        binding.tvOrganization.text = event.organization
        var phonesStr = ""
        event.contact_numbers.forEach {
            phonesStr += it + "\n"
        }
        binding.tvPhones.text = phonesStr
    }
}