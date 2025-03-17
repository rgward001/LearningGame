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

data class WordDefinition(val word: String, val definition: String, var streak: Int);

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
        loadStatsFromDisk()

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
                wordDefinition[0].streak += 1;
                if (totalStreak > highestStreak) {
                    highestStreak = totalStreak;
                }
                saveWordsToDisk();
            } else {
                totalWrong++;
                totalStreak = 0;
            }

            pickNewWordAndLoadDataList();
            myAdapter.notifyDataSetChanged();
        };
    }

    private fun saveWordsToDisk() {
        val file = File(applicationContext.filesDir, "user_data.csv")

        file.writeText("")

        for (word in wordDefinition) {
            file.appendText("${word.word}|${word.definition}|${word.streak}\n")
        }
    }

    private fun saveStatsToDisk() {
        val file = File(applicationContext.filesDir, "user_stats.csv")

        file.writeText(
            "Score:$score\n" +
                    "TotalCorrect:$totalCorrect\n" +
                    "TotalWrong:$totalWrong\n" +
                    "TotalStreak:$totalStreak\n" +
                    "HighestStreak:$highestStreak\n"
        )
    }

    private fun loadStatsFromDisk() {
        val file = File(applicationContext.filesDir, "user_stats.csv")

        if (file.exists()) {
            file.forEachLine { line ->
                val parts = line.split(":")
                if (parts.size == 2) {
                    when (parts[0]) {
                        "Score" -> score = parts[1].toInt()
                        "TotalCorrect" -> totalCorrect = parts[1].toInt()
                        "TotalWrong" -> totalWrong = parts[1].toInt()
                        "TotalStreak" -> totalStreak = parts[1].toInt()
                        "HighestStreak" -> highestStreak = parts[1].toInt()
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        saveWordsToDisk()
        saveStatsToDisk()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_WORD_CODE && resultCode == RESULT_OK && data != null){
            val word = data.getStringExtra("word")?:""
            val def = data.getStringExtra("def")?:""
            val streak = data.getIntExtra("streak", 0)

            Log.d("MAD", word)
            Log.d("MAD", def)
            Log.d("MAD", streak.toString())

            if ( word == "" || def == "")
                return

            wordDefinition.add(WordDefinition(word, def, streak))

            val file = File(applicationContext.filesDir, "user_data.csv");
            file.appendText("${word}|${def}|${streak}\n");

            val fileContent = file.readText();
            Log.d("Test", fileContent)

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
                wordDefinition.add(WordDefinition(wd[0], wd[1], wd[2].toInt()))
            }
        } else { // default data

            file.createNewFile()

            val reader = Scanner(resources.openRawResource(R.raw.default_words))
            while(reader.hasNextLine()){
                val line = reader.nextLine()
                val wd = line.split("|")
                wordDefinition.add(WordDefinition(wd[0], wd[1], wd[2].toInt()))
                file.appendText("${wd[0]}|${wd[1]}|${wd[2]}\n")
            }
        }
    }

    private fun pickNewWordAndLoadDataList() {
        val highPriorityWords = wordDefinition.filter { it.streak < 2 }.toMutableList()
        val lowPriorityWords = wordDefinition.filter { it.streak >= 2 }.toMutableList()

        highPriorityWords.shuffle()
        lowPriorityWords.shuffle()

        val selectedWord = if (highPriorityWords.isNotEmpty()) {
            highPriorityWords[0]
        } else {
            lowPriorityWords[0]
        }

        wordDefinition.remove(selectedWord)
        wordDefinition.add(0, selectedWord)

        dataDefList.clear()
        dataDefList.add(selectedWord.definition)

        val otherDefs = wordDefinition.filter { it.word != selectedWord.word }
            .shuffled()
            .take(3)
            .map { it.definition }

        dataDefList.addAll(otherDefs)
        dataDefList.shuffle()

        findViewById<TextView>(R.id.word).text = selectedWord.word
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