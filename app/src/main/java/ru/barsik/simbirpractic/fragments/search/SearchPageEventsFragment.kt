package ru.barsik.simbirpractic.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.databinding.FragmentSearchPageEventsBinding

class SearchPageEventsFragment : Fragment() {

    private lateinit var binding: FragmentSearchPageEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchPageEventsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(requireContext(), 1))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SearchRecyclerAdapter(
                resources.getStringArray(R.array.events_list).also { it.shuffle() })
        }
    }

}