package com.rale.mvvmroom

import com.rale.mvvmroom.db.entity.CommentEntity
import com.rale.mvvmroom.db.entity.ProductEntity
import java.util.*

object TestData {
    val PRODUCT1 = ProductEntity(1, "name", "desc", 3)
    val PRODUCT2 = ProductEntity(2, "name2", "desc2", 20)
    val PRODUCTS = arrayListOf(PRODUCT1, PRODUCT2)

    val COMMENT1 = CommentEntity(1, PRODUCT1.id, "desc", Date())
    val COMMENT2 = CommentEntity(2, PRODUCT2.id, "desc2", Date())
    val COMMENTS = arrayListOf(COMMENT1, COMMENT2)
}