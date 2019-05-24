package com.rale.mvvmroom.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.rale.mvvmroom.R

class MainActivity : AppCompatActivity(), OnProductSelectionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val fragment = ProductListFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment, ProductListFragment.TAG).commit()
        }
    }

    override fun onSelected(id: Int) {
        val productFragment = ProductFragment.forProduct(id)
        supportFragmentManager.beginTransaction().addToBackStack("product")
                .replace(R.id.fragment_container, productFragment, null).commit()
    }
}
