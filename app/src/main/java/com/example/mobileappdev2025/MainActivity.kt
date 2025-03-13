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
import java.io.File
import java.io.FileInputStream
import java.util.Random
import java.util.Scanner

data class WordDefinition(val word: String, val definition: String);

class MainActivity : AppCompatActivity() {
    private val ADD_WORD_CODE = 1234; // 1-65535
    private lateinit var myAdapter : ArrayAdapter<String>; // connect from data to gui
    private var dataDefList = ArrayList<String>(); // data
    private var wordDefinition = mutableListOf<WordDefinition>();
    private var score : Int = 0;
    private var totalCorrect : Int = 0;
    private var totalWrong : Int = 0;
    private var totalStreak : Int = 0;
    private var highestStreak : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadWordsFromDisk()

        pickNewWordAndLoadDataList();
        setupList();

        val defList = findViewById<ListView>(R.id.dynamic_def_list);
        defList.setOnItemClickListener { _, _, index, _ ->
            val selectedDefinition = dataDefList[index];
            val correctDefinition = wordDefinition[0].definition;

            if (selectedDefinition == correctDefinition) {
                totalStreak++;
                score += totalStreak;
                totalCorrect++;
                if (totalStreak > highestStreak) {
                    highestStreak = totalStreak;
                }
            } else {
                totalWrong++;
                totalStreak = 0;
            }

            pickNewWordAndLoadDataList();
            myAdapter.notifyDataSetChanged();
        };
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_WORD_CODE && resultCode == RESULT_OK && data != null){
            val word = data.getStringExtra("word")?:""
            val def = data.getStringExtra("def")?:""

            Log.d("MAD", word)
            Log.d("MAD", def)

            if ( word == "" || def == "")
                return

            wordDefinition.add(WordDefinition(word, def))

            pickNewWordAndLoadDataList()
            myAdapter.notifyDataSetChanged()
        }
    }

    private fun loadWordsFromDisk()
    {
        // user data
        val file = File(applicationContext.filesDir, "user_data.csv")

        if (file.exists()) {
            val readResult = FileInputStream(file)
            val scanner = Scanner(readResult)

            while(scanner.hasNextLine()){
                val line = scanner.nextLine()
                val wd = line.split("|")
                wordDefinition.add(WordDefinition(wd[0], wd[1]))
            }
        } else { // default data

            file.createNewFile()

            val reader = Scanner(resources.openRawResource(R.raw.default_words))
            while(reader.hasNextLine()){
                val line = reader.nextLine()
                val wd = line.split("|")
                wordDefinition.add(WordDefinition(wd[0], wd[1]))
                file.appendText("${wd[0]}|${wd[1]}\n")
            }
        }
    }

    private fun pickNewWordAndLoadDataList()
    {
        wordDefinition.shuffle();

        dataDefList.clear();

        for(wd in 0..3){
            dataDefList.add(wordDefinition[wd].definition);
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

    fun openStats(view: View)
    {
        val myIntent = Intent(this, StatsActivity::class.java)
        myIntent.putExtra("score", score.toString())
        myIntent.putExtra("totalCorrect", totalCorrect.toString())
        myIntent.putExtra("totalWrong", totalWrong.toString())
        myIntent.putExtra("totalStreak", totalStreak.toString())
        myIntent.putExtra("highestStreak", highestStreak.toString())
        startActivity(myIntent)
    }

    fun openAddWord(view : View)
    {
        var myIntent = Intent(this, AddWordActivity::class.java);
        startActivityForResult(myIntent, ADD_WORD_CODE)
    }
}