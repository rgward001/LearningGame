package com.example.mobileappdev2025

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Random

data class WordDefinition(val word: String, val definition: String);

class MainActivity : AppCompatActivity() {
    private lateinit var myAdapter : ArrayAdapter<String>; // connect from data to gui
    private var dataDefList = ArrayList<String>(); // data
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

        wordDefinition.add(
            WordDefinition(
                "orange",
                "orange def"
            )
        );

        pickNewWordAndLoadDataList();
        setupList();

        val defList = findViewById<ListView>(R.id.dynamic_def_list);
        defList.setOnItemClickListener { _, _, index, _ ->
            pickNewWordAndLoadDataList();
            myAdapter.notifyDataSetChanged();
        };
    }

    private fun pickNewWordAndLoadDataList()
    {
        wordDefinition.shuffle();

        dataDefList.clear();

        for(wd in wordDefinition){
            dataDefList.add(wd.definition);
        }

        findViewById<TextView>(R.id.word).text = wordDefinition[0].word;

        dataDefList.shuffle();
    }

    private fun setupList()
    {
        myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataDefList);

        // connect to list
        val defList = findViewById<ListView>(R.id.dynamic_def_list);
        defList.adapter = myAdapter;
    }

    fun openStats(view : View)
    {
        var myIntent = Intent(this, StatsActivity::class.java);
        startActivity(myIntent)
    }
}