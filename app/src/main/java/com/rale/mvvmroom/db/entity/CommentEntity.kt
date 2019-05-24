package com.rale.mvvmroom.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "comments",
        foreignKeys = [ForeignKey(entity = ProductEntity::class, parentColumns = ["id"], childColumns = ["productId"], onDelete = ForeignKey.CASCADE)],
        indices = [Index(value = ["productId"])])
data class CommentEntity(@PrimaryKey(autoGenerate = true) var id: Int?, val productId: Int, val text: String, val posted: Date)