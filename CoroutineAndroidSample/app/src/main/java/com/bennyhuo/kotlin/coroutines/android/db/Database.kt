package com.bennyhuo.kotlin.coroutines.android.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bennyhuo.kotlin.coroutines.android.appContext

@Database(entities = [User::class], version = 1)
abstract class Db : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        val instance by lazy {
            Room.databaseBuilder(appContext, Db::class.java, "user_db").build()
        }

        val userDao by lazy {
            Db.instance.userDao()
        }
    }
}