package com.example.roomdbstudy

import androidx.room.*

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insert(schedule: Schedule)   // 일정 추가

    @Query("SELECT * FROM schedule_table")
    suspend fun getAllSchedules(): List<Schedule>   // 모든 일정 가져오기

    @Delete
    suspend fun delete(schedule: Schedule)   // 일정 삭제
}