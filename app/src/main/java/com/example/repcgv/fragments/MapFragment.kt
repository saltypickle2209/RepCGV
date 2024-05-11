package com.example.repcgv.fragments

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.ImageURLAdapter
//TODO: uncomment this block when API is ready
//import com.example.repcgv.api.CinemaApi
//import com.example.repcgv.api.MovieApi
//import com.example.repcgv.api.RetrofitClient
//import com.example.repcgv.models.Cinema
import com.example.repcgv.models.Movie
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max
import kotlin.math.min

class MapFragment : Fragment() {
    private lateinit var menuBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var ticketBtn: ImageButton

    private lateinit var bottom_sheet_layout: CoordinatorLayout
    private lateinit var bottomSheet: ConstraintLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var viewPagerPromotion: ViewPager2
    private lateinit var promotionImageList: ArrayList<String>
    private lateinit var viewPagerPromotionAdapter: ImageURLAdapter

    private lateinit var textViewCinemaName: TextView
    private lateinit var textViewCinemaAddress: TextView

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val LOCATIONS = arrayOf(
        LatLng(10.764368270577801, 106.6823860622948),
        LatLng(10.763825353031713, 106.68746293969397),
        LatLng(10.770430176518737, 106.66989703617679)
    )
    //TODO: uncomment this block when API is ready
    //private lateinit var cinemas: ArrayList<Cinema>

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    private fun init(view: View){
        menuBtn = view.findViewById(R.id.menuBtn)
        backBtn = view.findViewById(R.id.backBtn)
        ticketBtn = view.findViewById(R.id.ticketBtn)

        menuBtn.setOnClickListener {
            (this.activity as? MainActivity)?.openDrawer()
        }

        backBtn.setOnClickListener {
            (this.activity as? MainActivity)?.goBack()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        //TODO: uncomment this block when API is ready
//        ticketBtn.setOnClickListener {
//            (this.activity as? MainActivity)?.addFragment(TicketFragment(), "ticket")
//        }

        viewPagerPromotion = view.findViewById(R.id.viewPagerPromotion)

        promotionImageList = ArrayList()
        promotionImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/i/m/imgpsh_fullsize_anim_2.png")
        promotionImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/9/8/980x448_4_-min_2.jpg")
        promotionImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/9/8/980x448_-min.png")
        promotionImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/9/8/980x448_-min_11.jpg")
        promotionImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/9/8/980x448_1_-min_4.png")
        promotionImageList.add("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/w/e/web_rolling_banner_980_x_448_px-min.png")

        viewPagerPromotionAdapter = ImageURLAdapter(this, promotionImageList)

        viewPagerPromotion.adapter = viewPagerPromotionAdapter
        viewPagerPromotion.offscreenPageLimit = 2
        viewPagerPromotion.clipToPadding = false
        viewPagerPromotion.clipChildren = false

        val advertisementTransformer = CompositePageTransformer()
        advertisementTransformer.addTransformer(MarginPageTransformer(30))
        viewPagerPromotion.setPageTransformer(advertisementTransformer)

        textViewCinemaName = view.findViewById(R.id.textViewCinemaName)
        textViewCinemaAddress = view.findViewById(R.id.textViewCinemaAddress)

        bottomSheet = view.findViewById(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        activity?.let{
            if(hasPermissions(activity as Context, PERMISSIONS)){
                getMap()
            }
            else{
                permReqLauncher.launch(PERMISSIONS)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        //TODO: uncomment this block when API is ready
//        cinemas.forEach {
//            val marker = googleMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)))
//            marker?.tag = it
//        }
//        googleMap.setOnMarkerClickListener {
//            val data = it.tag as Cinema
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(data.latitude, data.longitude), 15.0f))
//            textViewCinemaName.text = data.name
//            textViewCinemaAddress.text = data.address
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            it.hideInfoWindow()
//            true
//        }

        googleMap.setOnMapClickListener {
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f))
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        if(hasPermissions(activity as Context, PERMISSIONS)) {
            googleMap.isMyLocationEnabled = true
            zoomToCurrentPosition()
        }
    }

    private fun getMap(){
        //TODO: Uncomment this block when API is ready
//        val cinemaService = RetrofitClient.instance.create(CinemaApi::class.java)
//        val call = cinemaService.getAllCinemas()
//
//        call.enqueue(object : Callback<List<Cinema>> {
//            override fun onResponse(call: Call<List<Cinema>>, response: Response<List<Cinema>>) {
//                if (response.isSuccessful) {
//                    // Handle successful response
//                    cinemas = ArrayList(response.body()!!)
//                    val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//                    mapFragment?.getMapAsync(callback)
//                } else {
//                    (this@MapFragment.activity as? MainActivity)?.goBack()
//                }
//            }
//
//            override fun onFailure(call: Call<List<Cinema>>, t: Throwable) {
//                Log.i("API", t.message!!)
//            }
//        })
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private val permReqLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all{
            it.value
        }
        if(granted){
            Log.i("meo", "permission granted")
            getMap()
        }
        else{
            (this.activity as? MainActivity)?.goBack()
        }
    }

    @SuppressLint("MissingPermission")
    private fun zoomToCurrentPosition(){
        if(!hasPermissions(requireContext(), PERMISSIONS)){
            return
        }
        val task: Task<Location> = fusedLocationProviderClient.getLastLocation()
        task.addOnSuccessListener {
            if(it != null){
                currentLocation = it
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), 15.0f))
            }
        }
    }
}