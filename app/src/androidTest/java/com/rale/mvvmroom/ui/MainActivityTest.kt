package com.rale.mvvmroom.ui

import android.arch.core.executor.testing.CountingTaskExecutorRule
import android.arch.lifecycle.Observer
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import com.rale.mvvmroom.AppExecutors
import com.rale.mvvmroom.EspressoTestUtilTest
import com.rale.mvvmroom.R
import com.rale.mvvmroom.db.AppDatabase
import com.rale.mvvmroom.db.DATABASE_NAME
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class MainActivityTest {

    @get:Rule val activityRule = ActivityTestRule(MainActivity::class.java)

    @get:Rule val countingTaskExecutorRule = CountingTaskExecutorRule()

    init {
        InstrumentationRegistry.getTargetContext().deleteDatabase(DATABASE_NAME)
    }

    @Before
    fun disableRecyclerViewAnimation() {
        EspressoTestUtilTest.disableAnimations(activityRule)
    }

    @Before
    fun waitForDbCreation() {
        val latch = CountDownLatch(1)
        val databaseCreated = AppDatabase.getInstance(InstrumentationRegistry.getContext(), AppExecutors()).isDatabaseCreated
        activityRule.runOnUiThread({
            val observer = object : Observer<Boolean> {
                override fun onChanged(t: Boolean?) {
                    if (t == true) {
                        databaseCreated.removeObserver(this)
                        latch.countDown()
                    }
                }
            }
            databaseCreated.observeForever(observer)

        })
        assertThat("database should've initialized", latch.await(1, TimeUnit.MINUTES), `is`(true))
    }

    @Test
    fun clickOnFirstItem_opensComments() {
        drain()

        onView(withContentDescription(R.string.cd_products_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductAdapter.ViewHolder>(0, click()))

        drain()

        onView(withContentDescription(R.string.cd_comments_list)).check(matches(isDisplayed()))

        drain()

        onView(withContentDescription(R.string.cd_product_name)).check(matches(not(withText(""))))
    }

    private fun drain() {
        countingTaskExecutorRule.drainTasks(1, TimeUnit.MINUTES)
    }
}