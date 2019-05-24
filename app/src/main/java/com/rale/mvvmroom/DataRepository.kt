package com.rale.mvvmroom

import android.arch.lifecycle.MediatorLiveData
import com.rale.mvvmroom.db.AppDatabase
import com.rale.mvvmroom.db.entity.ProductEntity
import timber.log.Timber

class DataRepository {

    private val database: AppDatabase
    val products = MediatorLiveData<List<ProductEntity>>()

    private constructor(databaseIn: AppDatabase) {
        database = databaseIn

        products.addSource(database.productDao().loadAllProducts()) { productEntities ->
            database.isDatabaseCreated.value?.let {
                Timber.d("Is DatabaseCreated posted")
                products.postValue(productEntities)
            }
        }
    }

    fun loadProduct(productId: Int) = database.productDao().loadProduct(productId)

    fun loadComments(productId: Int) = database.commentDao().loadComments(productId)

    companion object {
        @Volatile private var INSTANCE: DataRepository? = null

        fun getInstance(database: AppDatabase): DataRepository = INSTANCE ?:
                synchronized(this) {
                    INSTANCE ?: DataRepository(database).also { INSTANCE = it }
                }
    }
}