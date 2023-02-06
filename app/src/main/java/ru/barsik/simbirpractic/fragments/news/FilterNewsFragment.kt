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
import ru.barsik.simbirpractic.dao.CategoryDAO
import ru.barsik.simbirpractic.databinding.FragmentFilterNewsBinding
import ru.barsik.simbirpractic.entity.Category

class FilterNewsFragment : Fragment() {

    private lateinit var binding: FragmentFilterNewsBinding
    private var categoryItemsList = ArrayList<Category>()
    private var resultList = ArrayList<Category>()
    private lateinit var categoryDAO: CategoryDAO
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterNewsBinding.inflate(layoutInflater)
        categoryDAO = CategoryDAO(requireContext())
        categoryItemsList = ArrayList(categoryDAO.getCategories())
        resultList = ArrayList(categoryDAO.getCategories())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = FilterAdapter(categoryItemsList)
        binding.topAppBar.setNavigationOnClickListener {
            setFragmentResult("filter", Bundle.EMPTY)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.submit_filter -> {
                    setFragmentResult("filter", Bundle().also { bundle ->
                        bundle.putIntegerArrayList(
                            BUNDLE_CATEGORIES_ID_LIST,
                            ArrayList(resultList.map { cat ->
                                cat.id
                            })
                        )
                    })
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
            true
        }
    }

    private inner class FilterAdapter(private val categoryItemsList: List<Category>) :
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
            holder.title.text = categoryItemsList[position].title
            holder.switch.setOnClickListener {
                val cat = categoryDAO.getCategoryByTitle(holder.title.text.toString())
                if (holder.switch.isChecked) resultList.add(cat)
                else resultList.remove(cat)
            }
        }

    }

    companion object {
        const val BUNDLE_CATEGORIES_ID_LIST = "CATEGORIES"
    }
}