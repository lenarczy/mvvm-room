package com.rale.mvvmroom.ui


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rale.mvvmroom.R
import com.rale.mvvmroom.db.entity.ProductEntity
import com.rale.mvvmroom.viewmodel.Product
import kotlinx.android.synthetic.main.product_item.view.*

class ProductAdapter(private val listener: (Int) -> Unit)
    : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val productList = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = productList.size

    fun setProducts(products: List<Product>) {
        productList.clear()
        productList.addAll(products)
    }

    class ViewHolder(private val mView: View, private val itemClick: (Int) -> Unit) : RecyclerView.ViewHolder(mView) {
        fun bind(product: Product) {
            with(product) {
                mView.productName.text = name
                mView.productPrice.text = price
                mView.productId.text = id
                mView.productDescription.text = description
                itemView.setOnClickListener { itemClick(id.toInt()) }
            }
        }
    }
}
