package com.example.roomdbstudy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var dao: ScheduleDao
    private lateinit var adapter: ScheduleAdapter

    private lateinit var titleEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        titleEditText = findViewById(R.id.titleEditText)
        descEditText = findViewById(R.id.descEditText)
        addButton = findViewById(R.id.addButton)
        recyclerView = findViewById(R.id.scheduleRecyclerView)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "schedule-db"
        ).fallbackToDestructiveMigration().build()
        dao = db.scheduleDao()


        adapter = ScheduleAdapter(mutableListOf()) { schedule ->
            val intent = Intent(this, ScheduleEditActivity::class.java)
            intent.putExtra("id", schedule.id)
            intent.putExtra("title", schedule.title)
            intent.putExtra("desc", schedule.desc)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        addButton.setOnClickListener {
            showAddScheduleDialog()
        }

        val deleteButton = findViewById<Button>(R.id.deleteButton)



        deleteButton.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            if (selectedItems.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    selectedItems.forEach { dao.delete(it) }
                    loadSchedules()  // 삭제 후 갱신
                }
            }
        }

        loadSchedules()
    }

    private fun loadSchedules() {
        CoroutineScope(Dispatchers.IO).launch {
            val scheduleList = dao.getAllSchedules()
            Log.d("MainActivity", "DB로부터 불러온 리스트: $scheduleList")
            withContext(Dispatchers.Main) {
                adapter.updateData(scheduleList)
            }
        }
    }

    private fun showAddScheduleDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_schedule, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.dialogTitleEditText)
        val descEditText = dialogView.findViewById<EditText>(R.id.dialogDescEditText)

        AlertDialog.Builder(this)
            .setTitle("새 일정 추가")
            .setView(dialogView)
            .setPositiveButton("추가") { _, _ ->
                val title = titleEditText.text.toString()
                val desc = descEditText.text.toString()
                if (title.isNotBlank() && desc.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.insert(Schedule(title = title, desc = desc))
                        loadSchedules()
                    }
                } else {
                    Toast.makeText(this, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }
    override fun onResume() {
        super.onResume()
        loadSchedules()  // 항상 최신 상태로 갱신
    }
}