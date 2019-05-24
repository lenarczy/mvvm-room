package com.rale.mvvmroom.db

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.rale.mvvmroom.AppExecutors
import com.rale.mvvmroom.DataGenerator
import com.rale.mvvmroom.db.dao.CommentDao
import com.rale.mvvmroom.db.dao.ProductDao
import com.rale.mvvmroom.db.entity.CommentEntity
import com.rale.mvvmroom.db.entity.ProductEntity
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val DATABASE_NAME = "basic-sample.db"

@Database(entities = [(ProductEntity::class), (CommentEntity::class)], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    val isDatabaseCreated = MutableLiveData<Boolean>()

    abstract fun productDao(): ProductDao
    abstract fun commentDao(): CommentDao

    private fun setDatabaseCreated() {
        isDatabaseCreated.postValue(true)
    }

    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            Timber.d("setDatabaseCreated from updateDatabaseCreated")
            setDatabaseCreated()
        }
    }

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, executors: AppExecutors): AppDatabase = INSTANCE ?:
            synchronized(this) {
                Timber.d("Instance is null $INSTANCE")
                INSTANCE ?: buildDatabase(context.applicationContext, executors).also {
                    it.updateDatabaseCreated(context.applicationContext)
                    INSTANCE = it
                    Timber.d("Instance is not null $INSTANCE")
                }
            }

        private fun buildDatabase(context: Context, executors: AppExecutors): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addCallback(object: Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            executors.diskIO.execute {
                                Timber.d("Start building database")
                                addDelay()
                                val database = AppDatabase.getInstance(context, executors)
                                val products = DataGenerator.generateProducts()
                                val comments = DataGenerator.generateCommentsForProducts(products)
                                insertData(database, products, comments)
                                database.setDatabaseCreated()
                            }
                        }
                    })
                    .build()
        }

        private fun insertData(database: AppDatabase, products: List<ProductEntity>, comments: List<CommentEntity>) {
            database.runInTransaction {
                database.productDao().insertAll(products)
                database.commentDao().insertAll(comments)
            }
        }
        private fun addDelay() {
            TimeUnit.SECONDS.sleep(4)
        }
    }
}