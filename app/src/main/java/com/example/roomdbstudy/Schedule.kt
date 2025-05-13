package com.example.roomdbstudy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // 자동 증가 ID
    val title: String,   // 일정 제목
    val desc: String     // 일정 내용
)