package com.rale.mvvmroom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

object EspressoTestUtilTest {

    fun <T : FragmentActivity>disableAnimations(activityTestRule: ActivityTestRule<T>) {
        activityTestRule.activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewCreated(fm: FragmentManager?, f: android.support.v4.app.Fragment?, v: View?, savedInstanceState: Bundle?) {
                        traverseViews(v)
                    }
                }, true
        )
    }

    private fun traverseViews(v: View?) {
        when(v) {
            is ViewGroup -> traverseViewGroup(v)
            is ProgressBar -> disableProgressBarAnimation(v)
            else -> {
                //do nothing
            }
        }

    }

    private fun traverseViewGroup(v: ViewGroup) {
        when(v) {
            is RecyclerView -> disableRecycleViewAnimations(v)
            else -> {
                val count = v.childCount
                for (i in 0..count) {
                    traverseViews(v.getChildAt(i))
                }
            }
        }
    }

    private fun disableProgressBarAnimation(v: ProgressBar) {
        v.indeterminateDrawable = ColorDrawable(Color.BLUE)
    }

    private fun disableRecycleViewAnimations(v: RecyclerView) {
        v.itemAnimator = null
    }
}