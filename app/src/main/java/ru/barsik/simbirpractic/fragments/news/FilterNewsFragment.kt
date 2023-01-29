package ru.barsik.simbirpractic.fragments.news

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.databinding.FragmentFilterNewsBinding

class FilterNewsFragment : Fragment() {

    private lateinit var binding: FragmentFilterNewsBinding
    private val categoryItemsList = ArrayList<Pair<String, Boolean>>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        resources.getStringArray(R.array.categories_names).map {
            categoryItemsList.add(Pair(it, true))
        }
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = FilterAdapter(categoryItemsList)
        binding.topAppBar.setNavigationOnClickListener {
            setFragmentResult("filter", Bundle.EMPTY)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.submit_filter -> {
                    val catTitles = ArrayList<String>()
                    val catSwitchers = ArrayList<Boolean>()
                    categoryItemsList.map {
                        catTitles.add(it.first)
                        catSwitchers.add(it.second)
                    }
                    setFragmentResult("filter", Bundle().also {
                        it.putStringArrayList(BUNDLE_CATEGORIES_LIST, catTitles)
                        it.putBooleanArray(BUNDLE_SWITCHES_LIST, catSwitchers.toBooleanArray())
                    })
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
            true
        }
    }

    private inner class FilterAdapter(private val categoryItemsList: List<Pair<String, Boolean>>) :
        RecyclerView.Adapter<FilterAdapter.CategoryFilterViewHolder>() {

        private inner class CategoryFilterViewHolder(itemView: View) : ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.tv_category_title)
            val switch: SwitchCompat = itemView.findViewById(R.id.switch_category)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CategoryFilterViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_filter_item, parent, false)
            return CategoryFilterViewHolder(itemView)
        }

        override fun getItemCount() = categoryItemsList.size

        override fun onBindViewHolder(holder: CategoryFilterViewHolder, position: Int) {
            holder.title.text = categoryItemsList[position].first
            holder.switch.isChecked = categoryItemsList[position].second
            holder.switch.setOnClickListener {
                this@FilterNewsFragment.categoryItemsList[position] =
                    Pair(categoryItemsList[position].first, (it as SwitchCompat).isChecked)
            }
        }

    }

    companion object {
        const val BUNDLE_CATEGORIES_LIST = "CATEGORIES"
        const val BUNDLE_SWITCHES_LIST = "SWITCHERS"
    }
}