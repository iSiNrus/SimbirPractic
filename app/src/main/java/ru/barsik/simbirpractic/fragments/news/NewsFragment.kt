package ru.barsik.simbirpractic.fragments.news

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.barsik.simbirpractic.MainActivity
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.dao.CategoryDAO
import ru.barsik.simbirpractic.databinding.FragmentNewsBinding
import ru.barsik.simbirpractic.entity.Category
import ru.barsik.simbirpractic.entity.Event
import ru.barsik.simbirpractic.fragments.EventInfoFragment
import ru.barsik.simbirpractic.util.NewsDiffUtil

class NewsFragment : Fragment() {

    private val TAG = "NewsFragment"
    private lateinit var binding: FragmentNewsBinding
    private var eventList: ArrayList<Event>? = null
    private lateinit var categoriesList: List<Category>
    private lateinit var adapter: NewsEventsAdapter

    var br: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == LoadEventsService.ACTION_UPDATE) {
                // Здесь можно обновлять UI
                eventList =
                    Gson().fromJson(
                        intent.extras?.getString(LoadEventsService.ACTION_UPDATE),
                        object : TypeToken<List<Event>>() {}.type
                    ) as ArrayList<Event>
                (requireActivity() as MainActivity).setEvents(eventList ?: ArrayList())
                initRecyclerView()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoriesList = CategoryDAO(requireContext()).getCategories()
        adapter = NewsEventsAdapter(eventList ?: ArrayList())

        setFragmentResultListener("filter") { _, bundle ->

            if (bundle.isEmpty) Log.d(TAG, "onCreateView: bundle is empty")
            else {
                val categoriesIdList =
                    bundle.getIntegerArrayList(FilterNewsFragment.BUNDLE_CATEGORIES_ID_LIST)

                if (categoriesIdList != null) {
                    eventList = eventList?.filter { event ->
                        event.categories.intersect(categoriesIdList).isNotEmpty()
                    } as ArrayList<Event>?
                }
            }
            (binding.rvNews.adapter as NewsEventsAdapter).setData(eventList ?: ArrayList())
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

    override fun onResume() {
        // сомнительное решение
        if (eventList == null) eventList = (requireActivity() as MainActivity).getEvents()
        if (eventList == null) {
            Log.d(TAG, "onViewCreated: old List NOT Detected")
            activity?.registerReceiver(
                br,
                IntentFilter().also { it.addAction(LoadEventsService.ACTION_UPDATE) })
            Log.d(TAG, "onResume: startService")
            (requireActivity() as MainActivity).startServiceForEvents()
        } else
            initRecyclerView()
        super.onResume()
    }

    private fun initRecyclerView() {
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.adapter = adapter
        (binding.rvNews.adapter as NewsEventsAdapter).setData(eventList ?: ArrayList())
        binding.pbNews.visibility = View.GONE
        binding.rvNews.visibility = View.VISIBLE
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