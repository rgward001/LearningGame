package com.example.mobileappdev2025

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Random

class MainActivity : AppCompatActivity() {
    private var leftNum :Int = 0;
    private var rightNum :Int = 0;

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

        var leftButton = findViewById<Button>(R.id.left_number_button)
        var rightButton = findViewById<Button>(R.id.right_number_button)

        var rand = Random()

        leftNum = rand.nextInt(10)
        rightNum = rand.nextInt(10)

        leftButton.text = leftNum.toString()
        rightButton.text = "$rightNum"
    }

    fun leftButtonOnClick(view: View)
    {
        Log.d("mad", "Left")
    }

    fun rightButtonOnClick(view: View)
    {
        Log.d("mad", "Right")
    }
}