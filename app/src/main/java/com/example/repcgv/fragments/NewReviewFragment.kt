package com.example.repcgv.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.example.repcgv.R

class NewReviewFragment : Fragment() {

    private lateinit var root: View;
    private lateinit var textReview: EditText
    private lateinit var buttonSubmit: Button;
    private lateinit var buttonStarsList: ArrayList<AppCompatButton>

    private val COLOR_STAR_SELECTED = Color.parseColor("#FFEB3B")
    private val COLOR_STAR_NONE = Color.parseColor("#000000")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_new_review, container, false)
        init()

        return root;
    }

    private fun init()
    {
        textReview = root.findViewById(R.id.textReview);
        buttonSubmit = root.findViewById(R.id.buttonSubmit);
        buttonStarsList = arrayListOf(
            root.findViewById(R.id.buttonStar_0),
            root.findViewById(R.id.buttonStar_1),
            root.findViewById(R.id.buttonStar_2),
            root.findViewById(R.id.buttonStar_3),
            root.findViewById(R.id.buttonStar_4),
        )

        for(i in 0 until 5)
        {
            buttonStarsList[i].setOnClickListener({
                SetStarOnClick(i);
            })
        }
    }


    private fun SetStarOnClick(index: Int)
    {
        for(i in 0 until 5){
            buttonStarsList[i].backgroundTintList = ColorStateList.valueOf( COLOR_STAR_NONE);
        }

        for(i in 0 until index+1){
            buttonStarsList[i].backgroundTintList = ColorStateList.valueOf(COLOR_STAR_SELECTED);
        }
    }

}