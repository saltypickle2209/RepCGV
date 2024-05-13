package com.example.repcgv.fragments

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.api.GenreApi
//TODO: Uncomment the following import statement to import the Retrofit library
//import com.example.repcgv.api.GenreApi
import com.example.repcgv.api.MovieApi
import com.example.repcgv.api.PersonApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.models.Genre
import com.example.repcgv.models.Movie
import com.example.repcgv.models.Person
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MovieManagementDetailFragment : Fragment() {
    private lateinit var mode: String
    private var id: Int = 0

    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var textViewHeaderTitle: TextView

    private lateinit var cardView: CardView
    private lateinit var imageViewMoviePosterPreview: ImageView

    private lateinit var inputLayoutMovieName: TextInputLayout
    private lateinit var inputLayoutMovieDuration: TextInputLayout
    private lateinit var inputLayoutMovieRating: TextInputLayout

    private lateinit var editTextMovieName: EditText
    private lateinit var editTextMovieDuration: EditText
    private lateinit var editTextMovieRating: EditText
    private lateinit var editTextMovieClassification: EditText
    private lateinit var editTextMovieBanner: EditText
    private lateinit var editTextMovieGenres: EditText
    private lateinit var editTextMovieDirector: EditText
    private lateinit var editTextMovieActors: EditText
    private lateinit var editTextMovieOverview: EditText

    private lateinit var saveBtn: Button
    private lateinit var deleteBtn: Button

    private var movie: Movie? = null
    var chosenUri: Uri? = null
    var selectedGenres: ArrayList<Genre> = ArrayList()
    var selectedDirector: Person? = null
    var selectedActors: ArrayList<Person> = ArrayList()

    private val movieService = RetrofitClient.instance.create(MovieApi::class.java)
    private val genreService = RetrofitClient.instance.create(GenreApi::class.java)
    private val personService = RetrofitClient.instance.create(PersonApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_management_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        arguments?.takeIf { it.containsKey("type") }?.apply {
            mode = getString("type")!!
            if(mode == "edit"){
                textViewHeaderTitle.text = "Update a movie"
                saveBtn.text = "Update"
                deleteBtn.visibility = View.VISIBLE

                id = getInt("id")
                fetchData()
            }
            else{
                textViewHeaderTitle.text = "Add a new movie"
                saveBtn.text = "Add"
                deleteBtn.visibility = View.GONE
            }
        }
    }

    private fun fetchData(){
        val call = movieService.getMovieByID(id)

        call.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    movie = response.body()!!
                    setData()
                } else {
                    val errorMessage = response.message()
                    Log.i("API", errorMessage)
                    Log.i("API", "GET FAILED")
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })
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

        textViewHeaderTitle = view.findViewById(R.id.textViewHeaderTitle)

        cardView = view.findViewById(R.id.cardView)
        imageViewMoviePosterPreview = view.findViewById(R.id.imageViewMoviePosterPreview)

        cardView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        inputLayoutMovieName = view.findViewById(R.id.inputLayoutMovieName)
        inputLayoutMovieDuration = view.findViewById(R.id.inputLayoutMovieDuration)
        inputLayoutMovieRating = view.findViewById(R.id.inputLayoutMovieRating)

        editTextMovieName = view.findViewById(R.id.editTextMovieName)
        editTextMovieDuration = view.findViewById(R.id.editTextMovieDuration)
        editTextMovieRating = view.findViewById(R.id.editTextMovieRating)
        editTextMovieClassification = view.findViewById(R.id.editTextMovieClassification)
        editTextMovieBanner = view.findViewById(R.id.editTextMovieBanner)
        editTextMovieGenres = view.findViewById(R.id.editTextMovieGenres)
        editTextMovieDirector = view.findViewById(R.id.editTextMovieDirector)
        editTextMovieActors = view.findViewById(R.id.editTextMovieActors)
        editTextMovieOverview = view.findViewById(R.id.editTextMovieOverview)

        editTextMovieName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val movieName: String = s.toString().trim()
                if(movieName.isEmpty()){
                    inputLayoutMovieName.error = "This field cannot be empty"
                    inputLayoutMovieName.isErrorEnabled = true
                }
                else{
                    inputLayoutMovieName.error = ""
                    inputLayoutMovieName.isErrorEnabled = false
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        editTextMovieDuration.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val movieDuration: String = s.toString().trim()
                if(movieDuration.isEmpty()){
                    inputLayoutMovieDuration.error = "This field cannot be empty"
                    inputLayoutMovieDuration.isErrorEnabled = true
                }
                else{
                    inputLayoutMovieDuration.error = ""
                    inputLayoutMovieDuration.isErrorEnabled = false
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        editTextMovieRating.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val movieRating: String = s.toString().trim()
                if(movieRating.isEmpty()){
                    inputLayoutMovieRating.error = "This field cannot be empty"
                    inputLayoutMovieRating.isErrorEnabled = true
                }
                else{
                    inputLayoutMovieRating.error = ""
                    inputLayoutMovieRating.isErrorEnabled = false
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        editTextMovieClassification.setOnClickListener {
            val options = listOf<String>("P", "K", "T13", "T16", "T18")

            val dialogView = layoutInflater.inflate(R.layout.alert_dialog_radio_select, null)
            val dialogTitle = dialogView.findViewById<TextView>(R.id.textViewAlertDialogTitle)
            val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)

            dialogTitle.text = "Choose a classification"

            val black_color = ContextCompat.getColor(this.requireContext(), R.color.black)
            val checked_color = ContextCompat.getColor(this.requireContext(), R.color.red)
            val unchecked_color = ContextCompat.getColor(this.requireContext(), R.color.greytext)

            val colorStateList = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked)),
                intArrayOf(checked_color, unchecked_color)
            )

            for((index, option) in options.withIndex()){
                val radioButton = RadioButton(this.requireContext())
                radioButton.id = index
                radioButton.text = option
                radioButton.setTextColor(black_color)
                radioButton.layoutDirection = View.LAYOUT_DIRECTION_RTL
                radioButton.layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(R.dimen.radio_button_height)
                )
                CompoundButtonCompat.setButtonTintList(
                    radioButton,
                    colorStateList
                )
                radioGroup.addView(radioButton)
                if(option == editTextMovieClassification.text.toString()){
                    radioButton.isChecked = true
                }
            }

            val dialogBuilder = AlertDialog.Builder(this.requireContext())
                .setView(dialogView)
                .setCancelable(true)

            val dialog = dialogBuilder.create()

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = dialogView.findViewById<RadioButton>(checkedId)
                editTextMovieClassification.setText(selectedRadioButton.text)

                dialog?.dismiss()
            }

            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)
            dialog.show()
        }

        editTextMovieGenres.setOnClickListener {
            val call = genreService.getAllGenres()

            call.enqueue(object : Callback<List<Genre>> {
                override fun onResponse(call: Call<List<Genre>>, response: Response<List<Genre>>) {
                    if (response.isSuccessful) {
                        val options = response.body()!!

                        val dialogView = layoutInflater.inflate(R.layout.alert_dialog_checkbox_select, null)
                        val dialogTitle = dialogView.findViewById<TextView>(R.id.textViewAlertDialogTitle)
                        val linearLayout = dialogView.findViewById<LinearLayout>(R.id.linearLayout)
                        var dialogSaveBtn = dialogView.findViewById<Button>(R.id.saveBtn)

                        dialogTitle.text = "Choose genre(s)"

                        val black_color = ContextCompat.getColor(this@MovieManagementDetailFragment.requireContext(), R.color.black)
                        val checked_color = ContextCompat.getColor(this@MovieManagementDetailFragment.requireContext(), R.color.red)
                        val unchecked_color = ContextCompat.getColor(this@MovieManagementDetailFragment.requireContext(), R.color.greytext)

                        val colorStateList = ColorStateList(
                            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked)),
                            intArrayOf(checked_color, unchecked_color)
                        )

                        for((index, option) in options.withIndex()){
                            val checkBox = CheckBox(this@MovieManagementDetailFragment.requireContext())
                            checkBox.id = index
                            checkBox.text = option.name
                            checkBox.setTextColor(black_color)
                            checkBox.layoutDirection = View.LAYOUT_DIRECTION_RTL
                            checkBox.layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                resources.getDimensionPixelSize(R.dimen.radio_button_height)
                            )
                            CompoundButtonCompat.setButtonTintList(
                                checkBox,
                                colorStateList
                            )
                            linearLayout.addView(checkBox)
                            if(selectedGenres.any { it.id == option.id }){
                                checkBox.isChecked = true
                            }
                        }

                        val dialogBuilder = AlertDialog.Builder(this@MovieManagementDetailFragment.requireContext())
                            .setView(dialogView)
                            .setCancelable(true)

                        val dialog = dialogBuilder.create()

                        dialogSaveBtn.setOnClickListener {
                            selectedGenres.clear()
                            for (i in 0..<linearLayout.childCount){
                                val checkBox = linearLayout.getChildAt(i) as CheckBox
                                if(checkBox.isChecked){
                                    selectedGenres.add(options[checkBox.id])
                                }
                            }
                            editTextMovieGenres.setText(selectedGenres.joinToString("/") { it.name })

                            dialog?.dismiss()
                        }

                        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)
                        dialog.show()
                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.code()} - ${ response.errorBody()?.string() }", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Genre>>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })

        }

        editTextMovieDirector.setOnClickListener {
            val call = personService.getAllPeople()

            call.enqueue(object : Callback<List<Person>> {
                override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val options = response.body()!!

                        val dialogView = layoutInflater.inflate(R.layout.alert_dialog_radio_select, null)
                        val dialogTitle = dialogView.findViewById<TextView>(R.id.textViewAlertDialogTitle)
                        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)

                        dialogTitle.text = "Choose a director"

                        val black_color = ContextCompat.getColor(this@MovieManagementDetailFragment.requireContext(), R.color.black)
                        val checked_color = ContextCompat.getColor(this@MovieManagementDetailFragment.requireContext(), R.color.red)
                        val unchecked_color = ContextCompat.getColor(this@MovieManagementDetailFragment.requireContext(), R.color.greytext)

                        val colorStateList = ColorStateList(
                            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked)),
                            intArrayOf(checked_color, unchecked_color)
                        )

                        for((index, option) in options.withIndex()){
                            val radioButton = RadioButton(this@MovieManagementDetailFragment.requireContext())
                            radioButton.id = index
                            radioButton.text = option.name
                            radioButton.setTextColor(black_color)
                            radioButton.layoutDirection = View.LAYOUT_DIRECTION_RTL
                            radioButton.layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                resources.getDimensionPixelSize(R.dimen.radio_button_height)
                            )
                            CompoundButtonCompat.setButtonTintList(
                                radioButton,
                                colorStateList
                            )
                            radioGroup.addView(radioButton)
                            if(option.name == editTextMovieDirector.text.toString()){
                                radioButton.isChecked = true
                            }
                        }

                        val dialogBuilder = AlertDialog.Builder(this@MovieManagementDetailFragment.requireContext())
                            .setView(dialogView)
                            .setCancelable(true)

                        val dialog = dialogBuilder.create()

                        radioGroup.setOnCheckedChangeListener { group, checkedId ->
                            val selectedRadioButton = dialogView.findViewById<RadioButton>(checkedId)
                            selectedDirector = options[selectedRadioButton.id]
                            editTextMovieDirector.setText(selectedDirector!!.name)

                            dialog?.dismiss()
                        }

                        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)
                        dialog.show()
                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.code()} - ${ response.errorBody()?.string() }", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
        }

        editTextMovieActors.setOnClickListener {
            val call = personService.getAllPeople()

            call.enqueue(object : Callback<List<Person>> {
                override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val options = response.body()!!

                        val dialogView = layoutInflater.inflate(R.layout.alert_dialog_checkbox_select, null)
                        val dialogTitle = dialogView.findViewById<TextView>(R.id.textViewAlertDialogTitle)
                        val linearLayout = dialogView.findViewById<LinearLayout>(R.id.linearLayout)
                        var dialogSaveBtn = dialogView.findViewById<Button>(R.id.saveBtn)

                        dialogTitle.text = "Choose actor(s)"

                        val black_color = ContextCompat.getColor(this@MovieManagementDetailFragment.requireContext(), R.color.black)
                        val checked_color = ContextCompat.getColor(this@MovieManagementDetailFragment.requireContext(), R.color.red)
                        val unchecked_color = ContextCompat.getColor(this@MovieManagementDetailFragment.requireContext(), R.color.greytext)

                        val colorStateList = ColorStateList(
                            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked)),
                            intArrayOf(checked_color, unchecked_color)
                        )

                        for((index, option) in options.withIndex()){
                            val checkBox = CheckBox(this@MovieManagementDetailFragment.requireContext())
                            checkBox.id = index
                            checkBox.text = option.name
                            checkBox.setTextColor(black_color)
                            checkBox.layoutDirection = View.LAYOUT_DIRECTION_RTL
                            checkBox.layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                resources.getDimensionPixelSize(R.dimen.radio_button_height)
                            )
                            CompoundButtonCompat.setButtonTintList(
                                checkBox,
                                colorStateList
                            )
                            linearLayout.addView(checkBox)
                            if(selectedActors.any { it.id == option.id }){
                                checkBox.isChecked = true
                            }
                        }

                        val dialogBuilder = AlertDialog.Builder(this@MovieManagementDetailFragment.requireContext())
                            .setView(dialogView)
                            .setCancelable(true)

                        val dialog = dialogBuilder.create()

                        dialogSaveBtn.setOnClickListener {
                            selectedActors.clear()
                            for (i in 0..<linearLayout.childCount){
                                val checkBox = linearLayout.getChildAt(i) as CheckBox
                                if(checkBox.isChecked){
                                    selectedActors.add(options[checkBox.id])
                                }
                            }
                            editTextMovieActors.setText(selectedActors.joinToString("/") { it.name })

                            dialog?.dismiss()
                        }

                        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)
                        dialog.show()
                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.code()} - ${ response.errorBody()?.string() }", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
        }

        saveBtn = view.findViewById(R.id.saveBtn)

        saveBtn.setOnClickListener {
            saveAction()
        }

        deleteBtn = view.findViewById(R.id.deleteBtn)

        deleteBtn.setOnClickListener {
            deleteAction()
        }
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            chosenUri = uri
            Glide.with(this.requireContext()).load(uri).into(imageViewMoviePosterPreview)
            if(imageViewMoviePosterPreview.visibility == View.GONE){
                imageViewMoviePosterPreview.visibility = View.VISIBLE
            }
        }
    }

    private fun saveAction(){
        if(inputLayoutMovieName.isErrorEnabled || inputLayoutMovieDuration.isErrorEnabled || inputLayoutMovieRating.isErrorEnabled
            || editTextMovieName.text.isEmpty() || editTextMovieDuration.text.isEmpty() || editTextMovieRating.text.isEmpty()
            || editTextMovieClassification.text.isEmpty() || editTextMovieBanner.text.isEmpty() || editTextMovieGenres.text.isEmpty()
            || editTextMovieDirector.text.isEmpty() || editTextMovieActors.text.isEmpty()
            || (mode == "add" && chosenUri == null)){
            Toast.makeText(this.requireContext(), "You haven't completed the form yet", Toast.LENGTH_SHORT).show()
        }
        else{
            var poster: MultipartBody.Part? = null
            var id: RequestBody? = null

            if(chosenUri != null) {
                val inputStream = requireContext().contentResolver.openInputStream(chosenUri!!)
                val file = File(requireContext().cacheDir, "poster_file")
                inputStream.use { input ->
                    file.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
//                val requestFile = RequestBody.create(file.toMediaType(), file)

                poster = MultipartBody.Part.createFormData("poster", file.name, requestFile)
            }

            if(movie != null){
                id = RequestBody.create("text/plain".toMediaTypeOrNull(), movie!!.id.toString())
            }

            val title = RequestBody.create("text/plain".toMediaTypeOrNull(), editTextMovieName.text.toString())
            val runTime = RequestBody.create("text/plain".toMediaTypeOrNull(), editTextMovieDuration.text.toString())
            val voteAverage = RequestBody.create("text/plain".toMediaTypeOrNull(), editTextMovieRating.text.toString())
            val classification = RequestBody.create("text/plain".toMediaTypeOrNull(), editTextMovieClassification.text.toString())
            val backdropPath = RequestBody.create("text/plain".toMediaTypeOrNull(), editTextMovieBanner.text.toString())
            val genreIds = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedGenres.joinToString(",") { it.id.toString() })
            val director = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedDirector!!.id.toString())
            val actors = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedActors.joinToString(",") { it.id.toString() })
            val overview = RequestBody.create("text/plain".toMediaTypeOrNull(), editTextMovieOverview.text.toString())

            val call = movieService.uploadMovie(poster, id, backdropPath, genreIds, title, overview, voteAverage, actors, director, runTime, classification)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        Toast.makeText(
                            requireContext(),
                            "Successfully added a new movie.",
                            Toast.LENGTH_SHORT
                        ).show()
                        (this@MovieManagementDetailFragment.activity as? MainActivity)?.goBack()
                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.code()} - ${ response.errorBody()?.string() }", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
        }
    }

    private fun deleteAction() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this.requireContext(), R.style.DialogTheme))

        builder.setTitle("Confirm deletion").setMessage("Are you sure you want to proceed?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val call = movieService.deleteMovieByID(movie!!.id)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        Toast.makeText(
                            requireContext(),
                            "Successfully deleted movie with ID ${movie!!.id}.",
                            Toast.LENGTH_SHORT
                        ).show()
                        (this@MovieManagementDetailFragment.activity as? MainActivity)?.goBack()
                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.code()} - ${ response.errorBody()?.string() }", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun setData(){
        Glide.with(this.requireContext()).load(movie?.poster).into(imageViewMoviePosterPreview)
        if(imageViewMoviePosterPreview.visibility == View.GONE){
            imageViewMoviePosterPreview.visibility = View.VISIBLE
        }
        editTextMovieName.setText(movie?.name)
        editTextMovieDuration.setText(movie?.duration.toString())
        editTextMovieRating.setText(movie?.voteAverage.toString())
        editTextMovieClassification.setText(movie?.classification)
        editTextMovieBanner.setText(movie?.backdropPath)
        editTextMovieOverview.setText(movie?.overview)

        fetchGenresFromIds(movie!!.genreIds) { genres, error ->
            if(error != null){
                Toast.makeText(requireContext(), "Something went wrong while fetching data.", Toast.LENGTH_SHORT).show()
            }
            else {
                selectedGenres = genres!!
                editTextMovieGenres.setText(selectedGenres.joinToString("/") { it.name })
            }
        }

        fetchPersonFromId(movie!!.director) { director, error ->
            if(error != null){
                Toast.makeText(requireContext(), "Something went wrong while fetching data.", Toast.LENGTH_SHORT).show()
            }
            else {
                selectedDirector = director
                editTextMovieDirector.setText(selectedDirector?.name)
            }
        }

        fetchPeopleFromIds(movie!!.actors) { actors, error ->
            if(error != null){
                Toast.makeText(requireContext(), "Something went wrong while fetching data.", Toast.LENGTH_SHORT).show()
            }
            else {
                selectedActors = actors!!
                editTextMovieActors.setText(selectedActors.joinToString("/") { it.name })
            }
        }
    }

    private fun fetchGenresFromIds(genreIds: List<Int>, callback: (ArrayList<Genre>?, Throwable?) -> Unit) {
        val genres: ArrayList<Genre> = ArrayList()
        var remainingIds = genreIds.size

        val genreCallback = object : Callback<Genre> {
            override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
                if(response.isSuccessful) {
                    genres.add(response.body()!!)
                }
                remainingIds--
                if(remainingIds == 0) {
                    callback(genres, null)
                }
            }

            override fun onFailure(call: Call<Genre>, t: Throwable) {
                remainingIds--
                if(remainingIds == 0) {
                    callback(genres, t)
                }
            }
        }

        for (id in genreIds) {
            val call = genreService.getGenreByID(id)
            call.enqueue(genreCallback)
        }
    }

    private fun fetchPersonFromId(personId: Int, callback: (Person?, Throwable?) -> Unit) {
        val call = personService.getPersonByID(personId)
        call.enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if(response.isSuccessful) {
                    callback(response.body()!!, null)
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    private fun fetchPeopleFromIds(peopleIds: List<Int>, callback: (ArrayList<Person>?, Throwable?) -> Unit) {
        val people: ArrayList<Person> = ArrayList()
        var remainingIds = peopleIds.size

        val personCallback = object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if(response.isSuccessful) {
                    people.add(response.body()!!)
                }
                remainingIds--
                if(remainingIds == 0) {
                    callback(people, null)
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                remainingIds--
                if(remainingIds == 0) {
                    callback(people, t)
                }
            }
        }

        for (id in peopleIds) {
            val call = personService.getPersonByID(id)
            call.enqueue(personCallback)
        }
    }
}