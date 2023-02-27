package ru.barsik.simbirpractic.fragments.search

import android.content.res.Resources.Theme
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding.widget.RxSearchView
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.databinding.FragmentSearchBinding
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {

    private val TAG = "SearchFragment"
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchView: SearchView
    private lateinit var pagerAdapter: SearchPagerAdapter
    /*
    * TODO Переделать устаревший метод
    * */
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        requireActivity().menuInflater.inflate(R.menu.menu_top_search_appbar, menu)
//        val myActionMenuItem = menu.findItem(R.id.search)
//        searchView = myActionMenuItem.actionView as SearchView
//        com.jakewharton.rxbinding.support.v7.widget.RxSearchView.queryTextChangeEvents(searchView)
//            .observeOn(AndroidSchedulers.mainThread())
//            .debounce(5, TimeUnit.MILLISECONDS)
//            .subscribe {
//                val frag = childFragmentManager.findFragmentByTag("f${binding.viewPager2.currentItem}")
//                Log.d(TAG, "onCreateOptionsMenu: ${frag.toString()} ${it.toString()}")
//            }
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                if (!searchView.isIconified) {
//                    searchView.isIconified = true
//                }
//                myActionMenuItem.collapseActionView()
//                return false
//            }
//
//            override fun onQueryTextChange(s: String?): Boolean {
//                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
//                return false
//            }
//        })
//
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        searchView = binding.topAppBar.findViewById<SearchView>(R.id.search)
        com.jakewharton.rxbinding.support.v7.widget.RxSearchView
            .queryTextChanges(searchView)
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(5, TimeUnit.MILLISECONDS)
            .subscribe {
                val frag =
                    childFragmentManager.findFragmentByTag("f${binding.viewPager2.currentItem}")
                Log.d(TAG, "onCreateOptionsMenu: ${frag.toString()} ${it.toString()}")
            }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "По мероприятиям"
                1 -> "По НКО"
                else -> "Unknown"
            }
        }.attach()
    }

//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menuInflater.inflate(R.menu.menu_top_search_appbar, menu)
//        val myActionMenuItem = menu.findItem(R.id.search)
//        searchView = myActionMenuItem.actionView as SearchView
//        com.jakewharton.rxbinding.support.v7.widget.RxSearchView.queryTextChangeEvents(searchView)
//            .observeOn(AndroidSchedulers.mainThread())
//            .debounce(5, TimeUnit.MILLISECONDS)
//            .subscribe {
//                val frag = childFragmentManager.findFragmentByTag("f${binding.viewPager2.currentItem}")
//                Log.d(TAG, "onCreateOptionsMenu: ${frag.toString()} ${it.toString()}")
//            }
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                if (!searchView.isIconified) {
//                    searchView.isIconified = true
//                }
//                myActionMenuItem.collapseActionView()
//                return false
//            }
//
//            override fun onQueryTextChange(s: String?): Boolean {
//                val frag = childFragmentManager.findFragmentByTag("f${binding.viewPager2.currentItem}")
//                Log.d(TAG, "onCreateOptionsMenu: ${frag.toString()} $s")
//                return true
//            }
//        })
//    }
//
//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        TODO("Not yet implemented")
//    }

}