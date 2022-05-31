package com.example.websocket.stocklist.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 2,
    entities = [
        CacheDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUsersDao(): CacheDao

}