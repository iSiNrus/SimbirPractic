package ru.barsik.simbirpractic.fragments.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.entity.Event
import ru.barsik.simbirpractic.util.NewsDiffUtil

class SearchRecyclerAdapter(
    private var listItems: List<Event>
) :
    RecyclerView.Adapter<SearchRecyclerAdapter.SearchItemViewHolder>() {

    inner class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_search_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchItemViewHolder(view)
    }

    override fun getItemCount() = listItems.size

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.title.text = listItems[position].title
    }

    fun setData(newItemList: List<Event>) {
        val diffUtil = NewsDiffUtil(listItems, newItemList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        listItems = newItemList
        diffResult.dispatchUpdatesTo(this)
    }
}