package com.example.repcgv.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.repcgv.R
import com.example.repcgv.adapters.ImageURLAdapter
import com.example.repcgv.adapters.RecyclerViewNewsAdapter
import com.example.repcgv.adapters.SliderMenuAdapter
import com.example.repcgv.adapters.ViewPagerMovieAdapter
import com.example.repcgv.decorators.SpacingItemDecorator
import com.example.repcgv.models.Movie
import com.example.repcgv.models.News
import jp.wasabeef.glide.transformations.BlurTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class HomeFragment : Fragment() {
    private lateinit var menuBtn: ImageButton
    private lateinit var ticketBtn: ImageButton
    private lateinit var bookBtn: Button

    private lateinit var imageViewDashboardBackground: ImageView

    private lateinit var viewPagerAdvertisement: ViewPager2
    private lateinit var viewPagerMovieList: ViewPager2
    private lateinit var viewPagerPromotion: ViewPager2
    private lateinit var viewPagerSliderMenu: ViewPager2
    private lateinit var viewPagerSliderMenuIndicator: LinearLayout
    private lateinit var dotsImage: Array<ImageView>

    private lateinit var recyclerViewNews: RecyclerView

    private lateinit var textViewMovieName: TextView
    private lateinit var textViewMovieInfo: TextView
    private lateinit var movieClassification: Button

    private lateinit var movieImageList: ArrayList<Int>
    private lateinit var advertisementImageList: ArrayList<String>
    private lateinit var newsList: ArrayList<News>
    private lateinit var movieList: ArrayList<Movie>

    private lateinit var viewPagerMovieListAdapter: ViewPagerMovieAdapter
    private lateinit var viewPagerAdvertisementAdapter: ImageURLAdapter
    private lateinit var recyclerViewNewsAdapter: RecyclerViewNewsAdapter
    private lateinit var viewPagerSliderMenuAdapter: SliderMenuAdapter

    private lateinit var imageViewUserIcon: ImageView
    private lateinit var searchBtn: Button

    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData(view)
        init(view)
    }

    private fun fetchData(view: View){
//        val movieService = RetrofitClient.instance.create(MovieApi::class.java)
//        val call = movieService.getCurrentlyShowingMovies()
//
//        call.enqueue(object : Callback<List<Movie>> {
//            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
//                if (response.isSuccessful) {
//                    // Handle successful response
//                    movieList = ArrayList(response.body()!!)
//                    initViewPagers()
//                    handleViewPagerEvents()
//                    initRecyclerViews()
//                } else {
//                    val errorMessage = response.message()
//                    Log.i("API", errorMessage)
//                    Log.i("API", "GET FAILED")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
//                Log.i("API", t.message!!)
//            }
//        })
    }

    private fun init(view: View){
        imageViewDashboardBackground = view.findViewById(R.id.imageViewDashboardBackground)

        menuBtn = view.findViewById(R.id.menuBtn)
        ticketBtn = view.findViewById(R.id.ticketBtn)
        bookBtn = view.findViewById(R.id.bookBtn)

        viewPagerAdvertisement = view.findViewById(R.id.viewPagerAdvertisement)
        viewPagerMovieList = view.findViewById(R.id.viewPagerMovieList)
        viewPagerPromotion = view.findViewById(R.id.viewPagerPromotion)
        viewPagerSliderMenu = view.findViewById(R.id.viewPagerSliderMenu)
        viewPagerSliderMenuIndicator = view.findViewById(R.id.viewPagerSliderMenuIndicator)

        recyclerViewNews = view.findViewById(R.id.recyclerViewNews)

        textViewMovieName = view.findViewById(R.id.textViewMovieName)
        textViewMovieInfo = view.findViewById(R.id.textViewMovieInfo)
        movieClassification = view.findViewById(R.id.movieClassification)

        advertisementImageList = ArrayList()
        advertisementImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/i/m/imgpsh_fullsize_anim_2.png")
        advertisementImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/9/8/980x448_4_-min_2.jpg")
        advertisementImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/9/8/980x448_-min.png")
        advertisementImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/9/8/980x448_-min_11.jpg")
        advertisementImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/9/8/980x448_1_-min_4.png")
        advertisementImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/w/e/web_rolling_banner_980_x_448_px-min.png")

        newsList = ArrayList()
        newsList.add(News("LONG LONG LONG LONG LONG LONG LONG LONG TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/350_X_495.png"))
        newsList.add(News("LONG LONG LONG LONG LONG LONG LONG LONG TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/B_p_Cola_350x495.jpg"))
        newsList.add(News("SHORT TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/350_X_495.png"))
        newsList.add(News("SHORT TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/B_p_Cola_350x495.jpg"))
        newsList.add(News("LONG LONG LONG LONG LONG LONG LONG LONG TITLE", "https://iguov8nhvyobj.vcdn.cloud/media/wysiwyg/2024/032024/350_X_495.png"))


        imageViewUserIcon = view.findViewById(R.id.imageViewUserIcon)
        searchBtn = view.findViewById(R.id.searchBtn)

        menuBtn.setOnClickListener {
            //(this.activity as? MainActivity)?.openDrawer()
        }

        ticketBtn.setOnClickListener {
            //(this.activity as? MainActivity)?.addFragment(TicketFragment(), "ticket")
        }

        bookBtn.setOnClickListener{
            //TODO: GET MOVIE ID FROM VIEWPAGER
            val fragment = MovieDetailFragment()
            fragment.arguments = Bundle().apply {
                putInt("id", id)
            }
            //(this.activity as? MainActivity)?.addFragment(fragment, "movie_detail")
        }

        imageViewUserIcon.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            val token = sharedPref.getString("token", "") ?: ""
            if (token == "") {
                Toast.makeText(requireContext(), "You need to log in to use this feature!", Toast.LENGTH_SHORT).show()
                //(this.activity as? MainActivity)?.addFragment(LoginFragment(), "login")
            }
            else {
                //(this.activity as? MainActivity)?.addFragment(UserDashboardFragment(), "member")
            }
        }

        searchBtn.setOnClickListener {
            //(this.activity as? MainActivity)?.addFragment(MovieSearchFragment(), "search")
        }

        handler = Handler(Looper.myLooper()!!)
    }

    private fun initViewPagers() {
        // MOVIE LIST VIEWPAGER
        viewPagerMovieListAdapter = ViewPagerMovieAdapter(this, movieList)

        viewPagerMovieList.adapter = viewPagerMovieListAdapter
        viewPagerMovieList.offscreenPageLimit = 2
        viewPagerMovieList.clipToPadding = false
        viewPagerMovieList.clipChildren = false
        viewPagerMovieList.setCurrentItem(1, false)

        textViewMovieName.text = movieList[1].name
        textViewMovieInfo.text = movieList[1].premiereDate.split("T")[0]
        Glide.with(this)
            .load(movieList[1].poster)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(imageViewDashboardBackground)
        setMovieClassification(movieList[1])


        val movieListTransformer = CompositePageTransformer()
        movieListTransformer.addTransformer { page, position ->
            val absPosition = abs(position)
            page.rotationY = -15 * position
            val scaleFactor = 0.85f + (1 - absPosition) * 0.15f
            page.scaleY = scaleFactor
            page.scaleX = scaleFactor
            page.elevation = 0f
        }
        viewPagerMovieList.setPageTransformer(movieListTransformer)

        // ADVERTISEMENT VIEWPAGER
        viewPagerAdvertisementAdapter = ImageURLAdapter(this, advertisementImageList)

        viewPagerAdvertisement.adapter = viewPagerAdvertisementAdapter
        viewPagerAdvertisement.offscreenPageLimit = 2
        viewPagerAdvertisement.clipToPadding = false
        viewPagerAdvertisement.clipChildren = false
        viewPagerAdvertisement.setCurrentItem(1, false)

        val advertisementTransformer = CompositePageTransformer()
        advertisementTransformer.addTransformer(MarginPageTransformer(30))
        viewPagerAdvertisement.setPageTransformer(advertisementTransformer)

        // PROMOTION VIEWPAGER
        viewPagerPromotion.adapter = viewPagerAdvertisementAdapter
        viewPagerPromotion.offscreenPageLimit = 2
        viewPagerPromotion.clipToPadding = false
        viewPagerPromotion.clipChildren = false
        viewPagerPromotion.setCurrentItem(1, false)

        viewPagerPromotion.setPageTransformer(advertisementTransformer)

        // SLIDER MENU VIEWPAGER
        viewPagerSliderMenuAdapter = SliderMenuAdapter(childFragmentManager, lifecycle)

        viewPagerSliderMenuAdapter.addFragment(SliderMenuFirstFragment())
        viewPagerSliderMenuAdapter.addFragment(SliderMenuSecondFragment())

        viewPagerSliderMenu.adapter = viewPagerSliderMenuAdapter

        dotsImage = Array(viewPagerSliderMenuAdapter.itemCount) { ImageView(this.requireContext()) }

        dotsImage.forEach {
            it.setImageResource(R.drawable.dot_unselected)
            viewPagerSliderMenuIndicator.addView(it, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(6,0,6,0)
            })
        }
        dotsImage[0].setImageResource(R.drawable.dot_selected)

        handler?.postDelayed(runnable, 5000)
    }

    private fun handleViewPagerEvents() {
        // MOVIE LIST VIEWPAGER
        viewPagerMovieList.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val item = movieList[position]
                textViewMovieName.text = item.name
                textViewMovieInfo.text = item.premiereDate.split("T")[0]
                Glide.with(this@HomeFragment)
                    .load(item.poster)
                    .placeholder(R.drawable.black)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                    .into(imageViewDashboardBackground)
                setMovieClassification(item)
            }
        })

        viewPagerMovieListAdapter.onItemClick = { id ->
            val fragment = MovieDetailFragment()
            fragment.arguments = Bundle().apply {
                putInt("id", id)
            }
            //(this.activity as? MainActivity)?.addFragment(fragment, "movie_detail")
        }

        // SLIDER MENU VIEWPAGER
        viewPagerSliderMenu.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                dotsImage.mapIndexed{ index, imageView ->
                    if(position == index){
                        imageView.setImageResource(R.drawable.dot_selected)
                    }
                    else{
                        imageView.setImageResource(R.drawable.dot_unselected)
                    }
                }
                super.onPageSelected(position)
            }
        })

        addInfiniteScroll(viewPagerMovieList)
        addInfiniteScroll(viewPagerAdvertisement)

        // ADVERTISEMENT VIEWPAGER
        viewPagerAdvertisement.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler?.removeCallbacks(runnable)
                handler?.postDelayed(runnable, 5000)
            }
        })
    }

    private fun setMovieClassification(item: Movie){
        if(item.classification == null)
            return
        when(item.classification){
            "P" -> {
                movieClassification.text = "P"
                movieClassification.setTextColor(Color.parseColor("#799D46"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#799D46"))
            }
            "K" -> {
                movieClassification.text = "K"
                movieClassification.setTextColor(Color.parseColor("#1A9ABD"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#1A9ABD"))
            }
            "T13" -> {
                movieClassification.text = "T13"
                movieClassification.setTextColor(Color.parseColor("#E8E10A"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8E10A"))
            }
            "T16" -> {
                movieClassification.text = "T16"
                movieClassification.setTextColor(Color.parseColor("#F3A001"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F3A001"))
            }
            "T18" -> {
                movieClassification.text = "T18"
                movieClassification.setTextColor(Color.parseColor("#EA3B24"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EA3B24"))
            }
            else -> {
                movieClassification.text = "U"
                movieClassification.setTextColor(Color.parseColor("#000000"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            }
        }
    }

    private fun addInfiniteScroll(viewPager2: ViewPager2){
        val recyclerView = viewPager2.getChildAt(0) as RecyclerView
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val itemCount = viewPager2.adapter?.itemCount ?: 0

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
                val lastItemVisible = layoutManager.findLastVisibleItemPosition()
                if(firstItemVisible == (itemCount - 1) && dx > 0){
                    recyclerView.scrollToPosition(1)
                }
                else if(lastItemVisible == 0 && dx < 0){
                    recyclerView.scrollToPosition(itemCount - 2)
                }
            }
        })
    }

    private fun initRecyclerViews(){
        recyclerViewNewsAdapter = RecyclerViewNewsAdapter(this, newsList)
        recyclerViewNews.adapter = recyclerViewNewsAdapter
        recyclerViewNews.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNews.addItemDecoration(SpacingItemDecorator(this.requireContext(), 50))
    }

    private val runnable = Runnable{
        viewPagerAdvertisement.currentItem = viewPagerAdvertisement.currentItem + 1
    }

    override fun onPause() {
        super.onPause()
        handler?.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler?.postDelayed(runnable, 5000)
    }
}