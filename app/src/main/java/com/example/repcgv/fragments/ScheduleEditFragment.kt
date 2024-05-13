package com.example.repcgv.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import com.bumptech.glide.Glide
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.api.CinemaApi
import com.example.repcgv.api.MovieApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.api.RoomApi
import com.example.repcgv.api.ScheduleApi
import com.example.repcgv.helper.Helper.Companion.enqueueWithLifecycle
import com.example.repcgv.models.Cinema
import com.example.repcgv.models.Movie
import com.example.repcgv.models.Room
import com.example.repcgv.models.Schedule
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ScheduleEditFragment : Fragment() {

    private lateinit var root: View

    private lateinit var mode: String
    private var id: Int = 0

    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var textViewHeaderTitle: TextView

    private lateinit var cardView: CardView
    private lateinit var imageViewMoviePosterPreview: ImageView

    private lateinit var editTextMovieName: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var editTextCinema: EditText
    private lateinit var editTextRoom: EditText


    private val movieService = RetrofitClient.instance.create(MovieApi::class.java)
    private val roomService = RetrofitClient.instance.create(RoomApi::class.java)
    private val cinemaService = RetrofitClient.instance.create(CinemaApi::class.java)
    private val scheduleService = RetrofitClient.instance.create(ScheduleApi::class.java)

    var selectedMovie: Movie? = null;
    var selectedRoom: Room? = null;
    var selectedCinema: Cinema? = null;

    var schedule: Schedule? = null


    private lateinit var saveBtn: Button
    private lateinit var deleteBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_schedule_edit, container, false)
        init()
        arguments?.let {
            mode = it.getString("type")!!
            if (mode == "edit") {
                textViewHeaderTitle.text = "Update schedule"
                saveBtn.text = "Update"
                deleteBtn.visibility = View.VISIBLE

                id = it.getInt("id")
                fetchData()
            } else {
                textViewHeaderTitle.text = "Create a schedule"
                saveBtn.text = "Add"
                deleteBtn.visibility = View.GONE
            }
        }

        return root;
    }

    private fun init() {
        backBtn = root.findViewById(R.id.backBtn)
        menuBtn = root.findViewById(R.id.menuBtn)

        backBtn.setOnClickListener {
            (this.activity as? MainActivity)?.goBack()
        }

        menuBtn.setOnClickListener {
            (this.activity as? MainActivity)?.openDrawer()
        }

        textViewHeaderTitle = root.findViewById(R.id.textViewHeaderTitle)

        cardView = root.findViewById(R.id.cardView)
        imageViewMoviePosterPreview = root.findViewById(R.id.imageViewMoviePosterPreview)

        editTextMovieName = root.findViewById(R.id.editTextMovieName)
        editTextDate = root.findViewById(R.id.editTextDate)
        editTextTime = root.findViewById(R.id.editTextTime)
        editTextCinema = root.findViewById(R.id.editTextCinema)
        editTextRoom = root.findViewById(R.id.editTextRoom)

        saveBtn = root.findViewById(R.id.saveBtn)
        deleteBtn = root.findViewById(R.id.deleteBtn)

        saveBtn.setOnClickListener {
            saveAction()
        }

        deleteBtn.setOnClickListener {
            deleteAction()
        }

        setEditTextOnClick()
    }

    private fun setEditTextOnClick() {
        val fragment = this;

        editTextMovieName.setOnClickListener {
            val call = movieService.getAllMovies()

            call.enqueueWithLifecycle(this, object : Callback<List<Movie>> {
                override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val options = response.body()!!

                        val dialogView =
                            layoutInflater.inflate(R.layout.alert_dialog_radio_select, null)
                        val dialogTitle =
                            dialogView.findViewById<TextView>(R.id.textViewAlertDialogTitle)
                        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)

                        dialogTitle.text = "Choose a movie"

                        val black_color = ContextCompat.getColor(
                            this@ScheduleEditFragment.requireContext(),
                            R.color.black
                        )
                        val checked_color = ContextCompat.getColor(
                            this@ScheduleEditFragment.requireContext(),
                            R.color.red
                        )
                        val unchecked_color = ContextCompat.getColor(
                            this@ScheduleEditFragment.requireContext(),
                            R.color.greytext
                        )

                        val colorStateList = ColorStateList(
                            arrayOf(
                                intArrayOf(android.R.attr.state_checked),
                                intArrayOf(-android.R.attr.state_checked)
                            ),
                            intArrayOf(checked_color, unchecked_color)
                        )

                        for ((index, option) in options.withIndex()) {
                            val radioButton =
                                RadioButton(this@ScheduleEditFragment.requireContext())
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
                            if (option.name == editTextMovieName.text.toString()) {
                                radioButton.isChecked = true
                            }
                        }

                        val dialogBuilder =
                            AlertDialog.Builder(this@ScheduleEditFragment.requireContext())
                                .setView(dialogView)
                                .setCancelable(true)

                        val dialog = dialogBuilder.create()
                        radioGroup.setOnCheckedChangeListener { group, checkedId ->
                            val selectedRadioButton =
                                dialogView.findViewById<RadioButton>(checkedId)
                            selectedMovie = options[selectedRadioButton.id]
                            editTextMovieName.setText(selectedMovie!!.name)
                            Glide.with(fragment).load(selectedMovie?.poster)
                                .into(imageViewMoviePosterPreview)
                            dialog?.dismiss()
                        }

                        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)
                        dialog.show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
        }

        editTextCinema.setOnClickListener {
            val call = cinemaService.getAllCinemas()

            call.enqueueWithLifecycle(this, object : Callback<List<Cinema>> {
                override fun onResponse(
                    call: Call<List<Cinema>>,
                    response: Response<List<Cinema>>
                ) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val options = response.body()!!

                        val dialogView =
                            layoutInflater.inflate(R.layout.alert_dialog_radio_select, null)
                        val dialogTitle =
                            dialogView.findViewById<TextView>(R.id.textViewAlertDialogTitle)
                        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)

                        dialogTitle.text = "Choose a cinema"

                        val black_color = ContextCompat.getColor(
                            this@ScheduleEditFragment.requireContext(),
                            R.color.black
                        )
                        val checked_color = ContextCompat.getColor(
                            this@ScheduleEditFragment.requireContext(),
                            R.color.red
                        )
                        val unchecked_color = ContextCompat.getColor(
                            this@ScheduleEditFragment.requireContext(),
                            R.color.greytext
                        )

                        val colorStateList = ColorStateList(
                            arrayOf(
                                intArrayOf(android.R.attr.state_checked),
                                intArrayOf(-android.R.attr.state_checked)
                            ),
                            intArrayOf(checked_color, unchecked_color)
                        )

                        for ((index, option) in options.withIndex()) {
                            val radioButton =
                                RadioButton(this@ScheduleEditFragment.requireContext())
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
                            if (option.name == editTextMovieName.text.toString()) {
                                radioButton.isChecked = true
                            }
                        }

                        val dialogBuilder =
                            AlertDialog.Builder(this@ScheduleEditFragment.requireContext())
                                .setView(dialogView)
                                .setCancelable(true)

                        val dialog = dialogBuilder.create()
                        radioGroup.setOnCheckedChangeListener { group, checkedId ->
                            val selectedRadioButton =
                                dialogView.findViewById<RadioButton>(checkedId)
                            selectedCinema = options[selectedRadioButton.id]
                            editTextCinema.setText(selectedCinema!!.name)
                            dialog?.dismiss()
                        }

                        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)
                        dialog.show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Cinema>>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
        }

        editTextRoom.setOnClickListener {
            val call = roomService.GetAllRoom();

            call.enqueueWithLifecycle(this, object : Callback<List<Room>> {
                override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val options = response.body()!!

                        val dialogView =
                            layoutInflater.inflate(R.layout.alert_dialog_radio_select, null)
                        val dialogTitle =
                            dialogView.findViewById<TextView>(R.id.textViewAlertDialogTitle)
                        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)

                        dialogTitle.text = "Choose a cinema"

                        val black_color = ContextCompat.getColor(
                            this@ScheduleEditFragment.requireContext(),
                            R.color.black
                        )
                        val checked_color = ContextCompat.getColor(
                            this@ScheduleEditFragment.requireContext(),
                            R.color.red
                        )
                        val unchecked_color = ContextCompat.getColor(
                            this@ScheduleEditFragment.requireContext(),
                            R.color.greytext
                        )

                        val colorStateList = ColorStateList(
                            arrayOf(
                                intArrayOf(android.R.attr.state_checked),
                                intArrayOf(-android.R.attr.state_checked)
                            ),
                            intArrayOf(checked_color, unchecked_color)
                        )

                        for ((index, option) in options.withIndex()) {
                            val radioButton =
                                RadioButton(this@ScheduleEditFragment.requireContext())
                            radioButton.id = index
                            radioButton.text = "Room ${option.room_id}"
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
                            if ("Room ${option.room_id}" == editTextMovieName.text.toString()) {
                                radioButton.isChecked = true
                            }
                        }

                        val dialogBuilder =
                            AlertDialog.Builder(this@ScheduleEditFragment.requireContext())
                                .setView(dialogView)
                                .setCancelable(true)

                        val dialog = dialogBuilder.create()
                        radioGroup.setOnCheckedChangeListener { group, checkedId ->
                            val selectedRadioButton =
                                dialogView.findViewById<RadioButton>(checkedId)
                            selectedRoom = options[selectedRadioButton.id]
                            editTextRoom.setText("Room ${selectedRoom!!.room_id}")
                            dialog?.dismiss()
                        }

                        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)
                        dialog.show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
        }

        editTextDate.setOnClickListener {
            showDatePicker()
        }

        editTextTime.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Update EditText with the selected date
                val selectedDate = formatDate(year, monthOfYear, dayOfMonth)
                // Update EditText with the selected date
                editTextDate.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this.requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // Format the selected time
                val selectedTime = formatTime(hourOfDay, minute)
                // Update EditText with the selected time
                editTextTime.setText(selectedTime)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(calendar.time)
    }

    private fun fetchData() {
        val call = scheduleService.GetScheduleByID(id)

        call.enqueueWithLifecycle(this, object : Callback<Schedule> {
            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    schedule = response.body()!!
                    setData()
                } else {
                    val errorMessage = response.message()
                    Log.i("API", errorMessage)
                    Log.i("API", "GET FAILED")
                }
            }

            override fun onFailure(call: Call<Schedule>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })
    }

    private fun setData() {
        if (schedule == null) return
        val fragment = this
        movieService.getMovieByID(schedule!!.movieId).enqueueWithLifecycle(this ,object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    selectedMovie = response.body()
                    editTextMovieName.setText(selectedMovie?.name)
                    Glide.with(fragment.requireContext()).load(selectedMovie?.poster)
                        .into(imageViewMoviePosterPreview)
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                // Handle failure
            }
        })

        // Set date and time
        val date = Date(schedule?.scheduleDate?.times(1000) ?: 0)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        editTextDate.setText(
            dateFormat.format(
                date
            )
        )
        editTextTime.setText(schedule?.scheduleStart)

        // Set cinema
        cinemaService.getCinemaById(schedule!!.cinemaId).enqueueWithLifecycle(this, object : Callback<Cinema> {
            override fun onResponse(call: Call<Cinema>, response: Response<Cinema>) {
                if (response.isSuccessful) {
                    selectedCinema = response.body()
                    editTextCinema.setText(selectedCinema?.name)
                }
            }

            override fun onFailure(call: Call<Cinema>, t: Throwable) {
                // Handle failure
            }
        })

        // Set room
        roomService.GetRoomById(schedule!!.roomId).enqueueWithLifecycle(this, object : Callback<Room> {
            override fun onResponse(call: Call<Room>, response: Response<Room>) {
                if (response.isSuccessful) {
                    selectedRoom = response.body()
                    editTextRoom.setText("Room ${schedule!!.roomId}")
                }
            }

            override fun onFailure(call: Call<Room>, t: Throwable) {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                editTextDate.setText(dateFormat.format(Date(schedule?.scheduleDate ?: 0)))
            }
        })
    }


    private fun saveAction() {

        val date = editTextDate.text.toString()

        val movieId = selectedMovie?.id
        val dateLong = dateToTimestamp(date) / 1000
        val time = editTextTime.text.toString()
        val cinemaId = selectedCinema?.id
        val roomId = editTextRoom.text.replace(Regex("[^0-9]"), "").toInt()

        if (movieId == null || date.isEmpty() || time.isEmpty() || cinemaId == null || roomId == null) {

            return
        }

        var scheduleId = Random.nextInt(Int.MAX_VALUE)
        if(schedule != null){
            scheduleId = schedule!!.scheduleId;
        }

        val schedule = Schedule(
            scheduleId = scheduleId,
            movieId = movieId,
            roomId = roomId,
            cinemaId = cinemaId,
            scheduleDate = dateLong,
            scheduleStart = time,
            seats = listOf()
        )
        val call = scheduleService.addSchedule(schedule)

        call.enqueueWithLifecycle(this, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    Toast.makeText(
                        requireContext(),
                        "Successfully add a schedule.",
                        Toast.LENGTH_SHORT
                    ).show()
                    (this@ScheduleEditFragment.activity as? MainActivity)?.goBack()
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.code()} - ${ response.errorBody()?.string() }", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Log failure message
                Log.e("API", "Failed to add schedule", t)
            }
        })
    }


    private fun deleteAction() {
        Log.i("API", "CALLED DELETE");
        val builder = AlertDialog.Builder(ContextThemeWrapper(this.requireContext(), R.style.DialogTheme))
        builder.setTitle("Confirm deletion").setMessage("Are you sure you want to proceed?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val call = scheduleService.deleteScheduleById(schedule!!.scheduleId)
            call.enqueueWithLifecycle(this, object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        Toast.makeText(
                            requireContext(),
                            "Successfully deleted schedule with ID ${schedule!!.scheduleId}.",
                            Toast.LENGTH_SHORT
                        ).show()
                        (this@ScheduleEditFragment.activity as? MainActivity)?.goBack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
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

    fun dateToTimestamp(dateString: String): Long {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        return date?.time ?: 0L
    }
}