package com.example.roomdbstudy

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleEditActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var saveButton: Button



    private lateinit var dao: ScheduleDao
    private var scheduleId: Int = -1  // 전달받은 ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)

        titleEditText = findViewById(R.id.editTitleEditText)
        descEditText = findViewById(R.id.editDescEditText)
        saveButton = findViewById(R.id.saveEditButton)

        dao = AppDatabase.getDatabase(this).scheduleDao()

        // 1. 인텐트에서 데이터 받기
        scheduleId = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title") ?: ""
        val desc = intent.getStringExtra("desc") ?: ""

        // 2. 화면에 표시
        titleEditText.setText(title)
        descEditText.setText(desc)

        // 3. 저장 버튼 클릭
        saveButton.setOnClickListener {
            Log.d("ScheduleEdit","수정 전: $title, $desc")
            val newTitle = titleEditText.text.toString()
            val newDesc = descEditText.text.toString()

            Log.d("ScheduleEdit", "수정 요청: $newTitle, $newDesc, id: $scheduleId")
            if (newTitle.isNotBlank() && newDesc.isNotBlank()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val updatedRows = dao.update(
                        Schedule(id = scheduleId, title = newTitle, desc = newDesc)
                    )
                    Log.d("ScheduleEdit", "수정된 row 수: $updatedRows")  // ← 이 로그 중요!!
                    finish()

                }
            } else {
                Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
