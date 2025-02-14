package com.example.mobileappdev2025

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Random

class MainActivity : AppCompatActivity() {
    private var leftNum :Int = 0;
    private var rightNum :Int = 0;
    private var score :Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // above init layout ui

        pickRandomNumber()
        setScore(0)
    }

    fun radioButtonOnClick(view: View)
    {
        if (view.id == R.id.radioButton){
            findViewById<TextView>(R.id.score_text).text = "R.id.radioButton"
        }

        if (view.id == R.id.radioButton2){
            findViewById<TextView>(R.id.score_text).text = "R.id.radioButton2"
        }

        if (view.id == R.id.radioButton3){
            findViewById<TextView>(R.id.score_text).text = "R.id.radioButton3"
        }
    }

    fun leftButtonOnClick(view: View)
    {
        if (leftNum > rightNum)
            setScore(score+1)
        else
            setScore(score-1)

        pickRandomNumber()
    }

    fun rightButtonOnClick(view: View)
    {
        if (leftNum < rightNum)
            setScore(score+1)
        else
            setScore(score-1)

        pickRandomNumber()
    }

    fun pickRandomNumber()
    {
        var leftButton = findViewById<Button>(R.id.left_number_button)
        var rightButton = findViewById<Button>(R.id.right_number_button)

        var rand = Random()

        do {
            leftNum = rand.nextInt(10)
            rightNum = rand.nextInt(10)
        } while (leftNum == rightNum)

        leftButton.text = "$leftNum"
        rightButton.text = "$rightNum"
    }

    fun setScore(_score: Int)
    {
        score = _score;

        // vari = (condition) ? true : false;

        findViewById<ImageView>(R.id.you_won_image).visibility = if (score > 5) View.VISIBLE else View.INVISIBLE;

        findViewById<TextView>(R.id.score_text).text = "Score: $score"
    }
}