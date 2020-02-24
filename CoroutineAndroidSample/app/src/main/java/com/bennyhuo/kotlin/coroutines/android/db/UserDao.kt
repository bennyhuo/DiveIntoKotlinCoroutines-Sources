package com.bennyhuo.kotlin.coroutines.android.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * from user")
    suspend fun listUsers(): List<User>

    companion object{
        suspend fun insertSampleData(){
            Db.userDao.run {
                insert(User(0, "Allan", 10))
                insert(User(1, "Becky", 12))
                insert(User(2, "Candy", 11))
                insert(User(3, "Donald", 18))
                insert(User(4, "Edward", 20))
                insert(User(5, "Frank", 20))
            }
        }
    }
}