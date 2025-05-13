package com.example.roomdbstudy

import androidx.room.*

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insert(schedule: Schedule)   // 일정 추가

    @Query("SELECT * FROM schedule")
    suspend fun getAllSchedules(): List<Schedule>   // 모든 일정 가져오기

    @Update
    suspend fun update(schedule: Schedule)   // 일정 업데이트 (Room이 자동으로 처리)

    @Delete
    suspend fun delete(schedule: Schedule)   // 일정 삭제
}
