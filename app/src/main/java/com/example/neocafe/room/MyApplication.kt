package com.example.neocafe.room

import android.app.Application

class MyApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}
