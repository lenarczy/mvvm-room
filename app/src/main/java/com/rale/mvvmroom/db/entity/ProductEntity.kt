package com.rale.mvvmroom.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(@PrimaryKey val id: Int, val name: String, val description: String, val price: Int)