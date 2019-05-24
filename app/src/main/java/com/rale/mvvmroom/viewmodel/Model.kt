package com.rale.mvvmroom.viewmodel

import com.rale.mvvmroom.db.entity.ProductEntity

data class Product(val id: String, val name: String, val description: String, val price: String)


fun convertProductEntitiesToModels(entities: List<ProductEntity>): List<Product> {
    return entities.map { convertProductEntityToModel(it) }
}

fun convertProductEntityToModel(entity: ProductEntity): Product {
    return Product("${entity.id}", entity.name, entity.description, "${entity.price}$")
}