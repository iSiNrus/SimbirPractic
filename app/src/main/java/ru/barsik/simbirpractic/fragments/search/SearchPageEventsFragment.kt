package ru.barsik.simbirpractic.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.barsik.simbirpractic.dao.EventDAO
import ru.barsik.simbirpractic.databinding.FragmentSearchPageEventsBinding

class SearchPageEventsFragment : Fragment(), SearchableFragment {

    private lateinit var binding: FragmentSearchPageEventsBinding
    private var _eventDAO: EventDAO? = null
    private val eventDAO : EventDAO get() { if(_eventDAO==null) _eventDAO = EventDAO(requireContext())
        return _eventDAO!!
    }
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
//            adapter = SearchRecyclerAdapter(
//                resources.getStringArray(R.array.events_list).also { it.shuffle() })
            adapter = SearchRecyclerAdapter(emptyList(), SearchRecyclerAdapter.TypeOfList.EVENTS)
        }
    }
    override fun setSearchQuery(query: String) {
        try {
            val resList = eventDAO.getEvents().filter{ x ->
                x.organization.contains(query, true)
            }
            if(query.isEmpty()){
                binding.searchContent.isVisible = false
                binding.clPlaceholder.isVisible = true
            } else {
                binding.searchContent.isVisible = true
                binding.clPlaceholder.isVisible = false
                if(resList.isEmpty())
                    binding.tvResultsOfSearch.text = "Результаты поиска: Ничего не найдено"
                else
                    binding.tvResultsOfSearch.text = "Результаты поиска: ${resList.size} мероприятий"

                (binding.recyclerView.adapter as SearchRecyclerAdapter).setData(resList)
            }
        } catch (e: Exception){
            Log.e("TAG", "setSearchQuery: CHPOK!", )
        }
    }

}