package com.rale.mvvmroom.db

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import com.rale.mvvmroom.LiveDataTestUtil
import com.rale.mvvmroom.TestData.COMMENT1
import com.rale.mvvmroom.TestData.COMMENTS
import com.rale.mvvmroom.TestData.PRODUCTS
import com.rale.mvvmroom.db.dao.CommentDao
import com.rale.mvvmroom.db.dao.ProductDao
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CommentDaoTest {

    @get:Rule val instantTaskExecutionRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    private lateinit var commentDao: CommentDao

    private lateinit var productDao: ProductDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).allowMainThreadQueries().build()
        commentDao = database.commentDao()
        productDao = database.productDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getCommentWhenNoCommentInserted() {
        val comments = LiveDataTestUtil.getValue(commentDao.loadComments(COMMENT1.productId))

        assertTrue(comments.isEmpty())
    }

    @Test(expected = SQLiteConstraintException::class)
    fun cantInsertCommentWithoutProduct() {
        commentDao.insertAll(COMMENTS)
    }

    @Test
    fun getCommentsAfterInserted() {
        productDao.insertAll(PRODUCTS)
        commentDao.insertAll(COMMENTS)

        val comments = LiveDataTestUtil.getValue(commentDao.loadComments(COMMENT1.productId))

        assertThat(comments.size, `is`(1))
    }
}