package com.rale.mvvmroom.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import android.databinding.ObservableField
import com.rale.mvvmroom.BasicApp
import com.rale.mvvmroom.DataRepository
import com.rale.mvvmroom.db.entity.CommentEntity

class ProductViewModel(application: Application, repository: DataRepository, productId: Int) : AndroidViewModel(application) {

    private val product = ObservableField<Product>()
    val observableProduct : LiveData<Product> = Transformations.map(repository.loadProduct(productId), ::convertProductEntityToModel)
    val observableComments : LiveData<List<CommentEntity>> = repository.loadComments(productId)

    fun setProduct(productEntity: Product) {
        product.set(productEntity)
    }

    class Factory(private val application: Application, private val productId: Int) : ViewModelProvider.NewInstanceFactory() {

        private val repository = (application as BasicApp).getRepository()

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (ProductViewModel(application, repository, productId) as T)
        }
    }
}