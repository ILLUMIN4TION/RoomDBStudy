package com.example.roomdbstudy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var addButton: Button
    private lateinit var loadButton: Button

    private val dao by lazy { MyApplication.database.scheduleDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleEditText = findViewById(R.id.editTitle)
        descEditText = findViewById(R.id.editDesc)
        resultTextView = findViewById(R.id.textResult)
        addButton = findViewById(R.id.btnAdd)
        loadButton = findViewById(R.id.btnLoad)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val desc = descEditText.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                dao.insert(Schedule(title = title, desc = desc))
            }
        }

        loadButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val schedules = dao.getAllSchedules()
                runOnUiThread {
                    resultTextView.text = schedules.joinToString("\n") {
                        "${it.id}: ${it.title} - ${it.desc}"
                    }
                }
            }
        }
    }
}