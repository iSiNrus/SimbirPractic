package ru.barsik.simbirpractic.fragments.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ru.barsik.simbirpractic.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private val TAG = "SearchFragment"
    private lateinit var binding: FragmentSearchBinding
    private lateinit var pagerAdapter: SearchPagerAdapter
    private val flow = MutableStateFlow("")

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flow.debounce(500).onEach {
            Log.d(TAG, "onEach: $it")
            pagerAdapter.getFragmentByPos(binding.tabLayout.selectedTabPosition)
                .setSearchQuery(it)
        }.launchIn(CoroutineScope(Dispatchers.Default))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = SearchPagerAdapter(this)
        binding.viewPager2.adapter = pagerAdapter

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit: GOT IT! $query")
                flow.value = query?:""
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit: chpok: $newText")
                flow.value = newText?:""
                return true
            }

        })
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "По мероприятиям"
                1 -> "По НКО"
                else -> "Unknown"
            }
        }.attach()
    }
}