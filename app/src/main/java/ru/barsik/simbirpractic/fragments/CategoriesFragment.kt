package ru.barsik.simbirpractic.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.barsik.simbirpractic.MainActivity
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.dao.CategoryDAO
import ru.barsik.simbirpractic.databinding.FragmentCategoriesBinding
import ru.barsik.simbirpractic.entity.Category
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CategoriesFragment() : Fragment() {

    private val TAG = "CategoriesFragment"
    private lateinit var binding: FragmentCategoriesBinding
    private var categories: ArrayList<Category>? = null
    private val task = Runnable {
        TimeUnit.SECONDS.sleep(2)
        val categoryDAO = CategoryDAO(requireContext())
        categories = categoryDAO.getCategories() as ArrayList
        handler.sendMessage(Message())
    }

    private val handler = Handler(Looper.getMainLooper()) {
        (requireActivity() as MainActivity).setCategories(categories?: ArrayList())
        initRecyclerView()
        return@Handler true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onResume() {
        categories = (requireActivity() as MainActivity).getCategories()
        if (categories == null) {
            Log.d(TAG, "onViewCreated: old List NOT Detected")
            val executor = Executors.newFixedThreadPool(1)
            executor.submit(task)
        } else
            initRecyclerView()
        super.onResume()
    }

    private fun initRecyclerView() {
        val categList = categories?.map { Pair(it.title, it.iconPath) }?.toList() ?: ArrayList()
        binding.categoryRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.categoryRecyclerview.adapter = CategoryItemAdapter(categList)
        binding.progressBarCategories.visibility = View.GONE
        binding.categoryRecyclerview.visibility = View.VISIBLE
    }

    private inner class CategoryItemAdapter(private val categoryList: List<Pair<String, String>>) :
        RecyclerView.Adapter<CategoryItemAdapter.CategoryViewHolder>() {

        inner class CategoryViewHolder(itemView: View) : ViewHolder(itemView) {
            val image: ImageView = itemView.findViewById(R.id.iv_cat_item)
            val title: TextView = itemView.findViewById(R.id.tv_cat_item_title)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.categories_item, parent, false)
            return CategoryViewHolder(itemView)
        }

        override fun getItemCount(): Int = categoryList.size

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.title.text = categoryList[position].first
            holder.image.setImageBitmap(
                BitmapFactory.decodeStream(
                    resources.assets.open(
                        categoryList[position].second
                    )
                )
            )
        }

    }
}