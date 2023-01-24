package ru.barsik.simbirpractic.fragments.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.barsik.simbirpractic.R

class SearchRecyclerAdapter(
    private val listItems: Array<String>
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
        holder.title.text = listItems[position]
    }
}