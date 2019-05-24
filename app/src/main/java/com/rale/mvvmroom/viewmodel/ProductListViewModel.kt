package com.rale.mvvmroom.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import com.rale.mvvmroom.BasicApp
import com.rale.mvvmroom.db.entity.ProductEntity

class ProductListViewModel(application: Application): AndroidViewModel(application) {

    val observableProducts = MediatorLiveData<List<Product>>()

    init {
        observableProducts.value = null
        val products: LiveData<List<ProductEntity>> = (application as BasicApp).getRepository().products
        val productsModel = Transformations.map(products, ::convertProductEntitiesToModels)
        observableProducts.addSource(productsModel, observableProducts::setValue)
    }
}