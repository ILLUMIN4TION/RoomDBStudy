package com.example.roomdbstudy

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "schedule_database"   // DB 파일 이름
        ).build()
    }
}