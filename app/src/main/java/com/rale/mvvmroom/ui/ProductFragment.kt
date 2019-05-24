package com.rale.mvvmroom.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rale.mvvmroom.R
import com.rale.mvvmroom.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.product_fragment.*
import kotlinx.android.synthetic.main.product_item.*
import timber.log.Timber
import kotlin.properties.Delegates

private const val ARG_PRODUCT_ID = "productId"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProductFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProductFragment.forProduct] factory method to
 * create an instance of this fragment.
 *
 */
class ProductFragment : Fragment() {
    private var productIdArg: Int? = null
    private var commentsAdapter by Delegates.notNull<CommentsAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productIdArg = it.getInt(ARG_PRODUCT_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.product_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commentsAdapter = CommentsAdapter()
        commentsList.adapter = commentsAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productIdArg?.let {
            val factory = ProductViewModel.Factory(activity!!.application, it)
            val viewModel = ViewModelProviders.of(this, factory).get(ProductViewModel::class.java)
            Timber.d("View model ref $viewModel")
            subscribeUi(viewModel)
        }
    }

    private fun subscribeUi(model: ProductViewModel) {
        model.observableProduct.observe(this, Observer {
            it?.let {
                model.setProduct(it)
                productName.text = it.name
                productPrice.text = it.price
                productId.text = it.id
                productDescription.text = it.description
            }
        })
        model.observableComments.observe(this, Observer {
            if (it == null) {
                commentsList.visibility = View.GONE
                loadingCommentsTv.visibility = View.VISIBLE
            } else {
                commentsList.visibility = View.VISIBLE
                loadingCommentsTv.visibility = View.GONE
                commentsAdapter.setComments(it)
            }
        })
    }

    companion object {

        @JvmStatic
        fun forProduct(productId: Int) =
                ProductFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PRODUCT_ID, productId)
                    }
                }
    }
}
