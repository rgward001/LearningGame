package com.example.mobileappdev2025

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Random

data class WordDefinition(val word: String, val definition: String);

class MainActivity : AppCompatActivity() {
    private lateinit var myAdapter : ArrayAdapter<String>;
    private  var dataDefList = ArrayList<String>();
    private var wordDefinition = mutableListOf<WordDefinition>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        wordDefinition.add(
            WordDefinition(
                "red",
                "red def"
            )
        );

        wordDefinition.add(
            WordDefinition(
                "yellow",
                "yellow def"
            )
        );

        wordDefinition.add(
            WordDefinition(
                "blue",
                "blue def"
            )
        );

        wordDefinition.add(
            WordDefinition(
                "green",
                "green def"
            )
        );
    }
}