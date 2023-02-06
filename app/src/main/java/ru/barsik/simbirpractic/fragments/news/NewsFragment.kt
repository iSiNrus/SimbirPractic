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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.barsik.simbirpractic.MainActivity
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.dao.CategoryDAO
import ru.barsik.simbirpractic.dao.EventDAO
import ru.barsik.simbirpractic.databinding.FragmentNewsBinding
import ru.barsik.simbirpractic.entity.Category
import ru.barsik.simbirpractic.entity.Event
import ru.barsik.simbirpractic.fragments.EventInfoFragment
import ru.barsik.simbirpractic.util.NewsDiffUtil

class NewsFragment : Fragment() {

    private val TAG = "NewsFragment"
    private lateinit var binding: FragmentNewsBinding
    private lateinit var eventList: List<Event>
    private lateinit var categoriesList: List<Category>
    private lateinit var adapter: NewsEventsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventList = EventDAO(requireContext()).getEvents()
        categoriesList = CategoryDAO(requireContext()).getCategories()
        adapter = NewsEventsAdapter(eventList)

        setFragmentResultListener("filter") { _, bundle ->

            with(requireActivity() as MainActivity) {
                this.switchFragment(NewsFragment(), addBackStack = false, showBottomNavigation = true)
            }
            if (bundle.isEmpty) Log.d(TAG, "onCreateView: bundle is empty")
            else {
                val categoriesIdList =
                    bundle.getIntegerArrayList(FilterNewsFragment.BUNDLE_CATEGORIES_ID_LIST)

                if (categoriesIdList != null) {
                    eventList = eventList.filter { event ->
                        event.categories.intersect(categoriesIdList).isNotEmpty()
                    }
                }
            }
            (binding.rvNews.adapter as NewsEventsAdapter).setData(eventList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.adapter = adapter
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

    }

    private inner class NewsEventsAdapter(private var itemList: List<Event>) :
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

        override fun getItemCount() = itemList.size
        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            holder.title.text = itemList[position].title
            holder.description.text = itemList[position].description
            holder.remainTime.text = "Not supported yet"
            holder.image.setImageBitmap(
                BitmapFactory.decodeStream(
                    requireContext().resources.assets.open(
                        itemList[position].title_img_path
                    )
                )
            )

            holder.itemView.setOnClickListener {
                (requireActivity() as MainActivity)
                    .switchFragment(
                        EventInfoFragment(itemList[position]),
                        addBackStack = true,
                        showBottomNavigation = false
                    )
            }
        }

        fun setData(newItemList: List<Event>) {
            val diffUtil = NewsDiffUtil(itemList, newItemList)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            itemList = newItemList
            diffResult.dispatchUpdatesTo(this)
        }

    }
}