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
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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
        (requireActivity() as MainActivity).setCategories(categories ?: ArrayList())
        initRecyclerView()
        Snackbar.make(binding.root, "Taken from file", Snackbar.LENGTH_SHORT).show()
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
            val categoryDAO = CategoryDAO(requireContext())
            val t = categoryDAO.getCategoryFromServer()
                .subscribe({
                    categories = it as ArrayList<Category>
                    initRecyclerView()
                    Snackbar.make(binding.root, "Taken from Firebase", Snackbar.LENGTH_SHORT).show()
                }, {
                    Log.e(TAG, "error on server observer: ${it.message}", it)
                    val executor = Executors.newFixedThreadPool(1)
                    executor.submit(task)
                })
        } else
            initRecyclerView()
        super.onResume()
    }

    private fun initRecyclerView() {
        val categList = categories?.map { Pair(it.title, it.icon_path) }?.toList() ?: ArrayList()
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
            val picFirebase = Firebase.storage
            picFirebase.maxOperationRetryTimeMillis = 2000
            val ref = picFirebase.getReference(categoryList[position].second)
            ref.downloadUrl.addOnSuccessListener {
                Log.d(TAG, "onBindViewHolder: Success")
                holder.image.load(it)
            }.addOnFailureListener {
                Log.d(TAG, "onBindViewHolder: Failure")
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
}