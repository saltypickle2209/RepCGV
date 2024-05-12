package com.example.repcgv.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.RecyclerViewNewsAdapter
import com.example.repcgv.adapters.RecyclerViewNewsLargeAdapter
import com.example.repcgv.decorators.SpacingItemDecorator
import com.example.repcgv.decorators.VerticalSpacingItemDecorator
import com.example.repcgv.models.News

class NewsAndPromosFragment : Fragment() {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var recyclerViewNews: RecyclerView
    private lateinit var recyclerViewNewsAdapter: RecyclerViewNewsLargeAdapter
    private lateinit var newsList: ArrayList<News>

    private lateinit var scrollView: NestedScrollView
    private lateinit var upwardBtn: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news_and_promos, container, false)

        init(view)

        return view
    }

    private fun init(view: View){
        backBtn = view.findViewById(R.id.backBtn)
        menuBtn = view.findViewById(R.id.menuBtn)

        backBtn.setOnClickListener {
            (this.activity as? MainActivity)?.goBack()
        }

        menuBtn.setOnClickListener {
            (this.activity as? MainActivity)?.openDrawer()
        }

        recyclerViewNews = view.findViewById(R.id.recyclerViewNews)

        newsList = ArrayList()
        newsList.add(News("LONG LONG LONG LONG LONG LONG LONG LONG TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/350_X_495.png"))
        newsList.add(News("LONG LONG LONG LONG LONG LONG LONG LONG TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/B_p_Cola_350x495.jpg"))
        newsList.add(News("SHORT TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/350_X_495.png"))
        newsList.add(News("SHORT TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/B_p_Cola_350x495.jpg"))
        newsList.add(News("LONG LONG LONG LONG LONG LONG LONG LONG TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/350_X_495.png"))

        recyclerViewNewsAdapter = RecyclerViewNewsLargeAdapter(this, newsList)
        recyclerViewNews.adapter = recyclerViewNewsAdapter
        recyclerViewNews.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerViewNews.addItemDecoration(VerticalSpacingItemDecorator(this.requireContext(), 30))

        scrollView = view.findViewById(R.id.scrollView)
        upwardBtn = view.findViewById(R.id.upwardBtn)

        upwardBtn.setOnClickListener {
            scrollView.smoothScrollTo(0, 0)
        }
    }
}