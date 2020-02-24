package com.bennyhuo.kotlin.coroutines.android.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user", primaryKeys = ["id"])
data class User(
    @ColumnInfo(name="id") val id: Long,
    @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="age") val age: Int)