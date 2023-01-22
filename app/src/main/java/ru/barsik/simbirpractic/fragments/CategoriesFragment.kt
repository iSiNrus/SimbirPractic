package ru.barsik.simbirpractic.fragments

import android.database.DataSetObserver
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val categList = arrayListOf<Pair<String, Int>>(
            Pair("Дети", R.drawable.children),
            Pair("Взрослые", R.drawable.adults),
            Pair("Пожилые", R.drawable.elderly),
            Pair("Животные", R.drawable.animals),
            Pair("Мероприятия", R.drawable.events),
            Pair("Дети", R.drawable.children),
            Pair("Взрослые", R.drawable.adults),
            Pair("Пожилые", R.drawable.elderly),
            Pair("Животные", R.drawable.animals),
            Pair("Мероприятия", R.drawable.events)
        )
        binding.categoryRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.categoryRecyclerview.addItemDecoration(object : ItemDecoration(){

        })
        binding.categoryRecyclerview.adapter = CategoryItemAdapter(categList)
    }

    private inner class CategoryItemAdapter(private val categoryList: List<Pair<String, Int>>) :
        RecyclerView.Adapter<CategoryItemAdapter.CategoryViewHolder>() {

        inner class CategoryViewHolder(itemView: View) : ViewHolder(itemView) {
            val image : ImageView = itemView.findViewById(R.id.iv_cat_item)
            val title : TextView = itemView.findViewById(R.id.tv_cat_item_title)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.categories_item, parent, false)
            return CategoryViewHolder(itemView)
        }

        override fun getItemCount(): Int = categoryList.size

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.title.text = categoryList[position].first
            holder.image.setImageResource(categoryList[position].second)
        }

    }
}