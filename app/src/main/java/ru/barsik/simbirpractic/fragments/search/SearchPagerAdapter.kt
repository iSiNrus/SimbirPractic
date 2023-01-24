package ru.barsik.simbirpractic.fragments.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> SearchPageEventsFragment()
            1 -> SearchPageNKOFragment()
            else -> throw Exception("Unsupported page position")
        }
    }

}