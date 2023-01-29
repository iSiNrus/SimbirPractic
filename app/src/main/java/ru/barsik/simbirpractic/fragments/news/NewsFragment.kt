package ru.barsik.simbirpractic.fragments.news

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.barsik.simbirpractic.MainActivity
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.dao.EventDAO
import ru.barsik.simbirpractic.databinding.FragmentNewsBinding
import ru.barsik.simbirpractic.entity.Event

class NewsFragment : Fragment() {

    private val TAG = "NewsFragment"
    private lateinit var binding: FragmentNewsBinding
    private lateinit var eventList: List<Event>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventList = EventDAO(requireContext()).getEvents()
        setFragmentResultListener("filter") { _, bundle ->
            parentFragmentManager.commit {
                replace(R.id.fragment_container, NewsFragment())
                (requireActivity() as MainActivity).showNavigation()
            }
            if (bundle.isEmpty) Log.d(TAG, "onCreateView: bundle is empty")
            else {
                val categoryList =
                    bundle.getStringArrayList(FilterNewsFragment.BUNDLE_CATEGORIES_LIST)
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

        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.adapter = NewsEventsAdapter(eventList)
    }

    private inner class NewsEventsAdapter(private val items: List<Event>) :
        RecyclerView.Adapter<NewsEventsAdapter.EventViewHolder>() {

        private inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.tv_news_title)
            val description: TextView = itemView.findViewById(R.id.tv_news_description)
            val image: ImageView = itemView.findViewById(R.id.news_image)
            val remainTime: TextView = itemView.findViewById(R.id.tv_news_remain_time)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.news_item_card, parent, false)
            return EventViewHolder(view)
        }

        override fun getItemCount() = items.size
        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            holder.title.text = items[position].title
            holder.description.text = items[position].description
            holder.remainTime.text = "Not supported yet"
            holder.image.setImageBitmap(
                BitmapFactory.decodeStream(
                    requireContext().resources.assets.open(
                        items[position].title_img_path
                    )
                )
            )
        }

    }
}