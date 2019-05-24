package com.rale.mvvmroom.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.rale.mvvmroom.db.entity.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun loadAllProducts(): LiveData<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM products WHERE id = :productId")
    fun loadProduct(productId: Int): LiveData<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun loadProductSync(productId: Int): ProductEntity
}