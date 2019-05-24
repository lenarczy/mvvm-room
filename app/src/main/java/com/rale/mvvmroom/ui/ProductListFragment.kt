package com.rale.mvvmroom.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rale.mvvmroom.R
import com.rale.mvvmroom.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.list_fragment.*
import kotlin.properties.Delegates

class ProductListFragment : Fragment() {

    private var productAdapter by Delegates.notNull<ProductAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productAdapter = ProductAdapter { productId ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                this@ProductListFragment.activity?.let {
                    if (it is OnProductSelectionListener) {
                        it.onSelected(productId)
                    }
                }
            }
        }
        productsList.adapter = productAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        subscribeUi(viewModel)
    }

    private fun subscribeUi(viewModel: ProductListViewModel) {
        viewModel.observableProducts.observe(this, Observer { products ->
            if (products != null) {
                loadingTv.visibility = View.GONE
                productAdapter.setProducts(products)
                productsList.visibility = View.VISIBLE
            } else {
                loadingTv.visibility = View.VISIBLE
                productsList.visibility = View.GONE
            }
        })
    }

    companion object {

        const val TAG = "com.rale.mvvmroom.viewmodel.ProductListViewModel"

        @JvmStatic
        fun newInstance() =
                ProductListFragment().apply {
                    arguments = Bundle()
                }
    }
}
