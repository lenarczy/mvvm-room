package com.rale.mvvmroom.db

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.rale.mvvmroom.LiveDataTestUtil
import com.rale.mvvmroom.TestData
import com.rale.mvvmroom.db.dao.ProductDao
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDaoTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    private lateinit var productDao: ProductDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).allowMainThreadQueries().build()
        productDao = database.productDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getProductsWhenNoProductInserted() {
        val products = LiveDataTestUtil.getValue(productDao.loadAllProducts())

        assertTrue(products.isEmpty())
    }

    @Test
    fun getProductsAfterInserted() {
        productDao.insertAll(TestData.PRODUCTS)

        val products = LiveDataTestUtil.getValue(productDao.loadAllProducts())

        assertThat(products.size, `is`(TestData.PRODUCTS.size))
    }

    @Test
    fun getProductById() {
        productDao.insertAll(TestData.PRODUCTS)

        val product = LiveDataTestUtil.getValue(productDao.loadProduct(TestData.PRODUCT1.id))

        assertThat(product.id, `is`(TestData.PRODUCT1.id))
        assertThat(product.name, `is`(TestData.PRODUCT1.name))
        assertThat(product.description, `is`(TestData.PRODUCT1.description))
        assertThat(product.price, `is`(TestData.PRODUCT1.price))
    }
}