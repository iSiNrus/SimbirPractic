package ru.barsik.simbirpractic.fragments.news

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import ru.barsik.simbirpractic.MainActivity
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private val TAG = "NewsFragment"
    private lateinit var binding: FragmentNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("filter"){ _, bundle ->
            parentFragmentManager.commit {
                replace(R.id.fragment_container, NewsFragment())
                (requireActivity() as MainActivity).showNavigation()
            }
            if(bundle.isEmpty) Log.d(TAG, "onCreateView: bundle is empty")
            else {
                val categoryList = bundle.getStringArrayList(FilterNewsFragment.BUNDLE_CATEGORIES_LIST)
                val switchesArray = bundle.getBooleanArray(FilterNewsFragment.BUNDLE_SWITCHES_LIST)
                categoryList?.forEachIndexed { index, s ->
                    Log.d(TAG, "onCreateView: $index $s - ${switchesArray?.get(index)}")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_filter -> {
                    (requireActivity() as MainActivity).hideNavigation()
                    parentFragmentManager.commit {
                        replace(R.id.fragment_container, FilterNewsFragment())
                        addToBackStack(null)
                    }
                }
            }
            true
        }
    }

}