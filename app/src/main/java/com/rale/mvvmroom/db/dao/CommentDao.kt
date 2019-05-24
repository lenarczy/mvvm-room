package com.rale.mvvmroom.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.rale.mvvmroom.db.entity.CommentEntity

@Dao
interface CommentDao {

    @Query("SELECT * FROM comments WHERE productId = :productId")
    fun loadComments(productId: Int): LiveData<List<CommentEntity>>

    @Query("SELECT * FROM comments WHERE productId = :productId")
    fun loadCommentsSync(productId: Int): LiveData<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(comments: List<CommentEntity>)
}