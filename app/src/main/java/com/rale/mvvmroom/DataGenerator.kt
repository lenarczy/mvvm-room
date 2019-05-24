package com.rale.mvvmroom

import com.rale.mvvmroom.db.entity.CommentEntity
import com.rale.mvvmroom.db.entity.ProductEntity
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

object DataGenerator {

    private val FIRST = listOf("Special edition", "New", "Cheap", "Quality", "Used")
    private val SECOND = listOf("Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle")
    private val DESCRIPTION = listOf("is finally here", "is recommended by Stan S. Stanman",
            "is the best sold product on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine")
    private val COMMENTS = listOf("Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6")

    fun generateProducts(): List<ProductEntity> {
        val products = ArrayList<ProductEntity>()
        val rnd = Random()
        var i = 0
        FIRST.forEachIndexed { i, first ->
            SECOND.forEachIndexed { j, second ->
                val id = FIRST.size * i + j + 1
                val name = "$first $second"
                val productEntity = ProductEntity(id, name, "$name ${DESCRIPTION[j]}", rnd.nextInt(240))
                products.add(productEntity)
            }
        }
        return products
    }

    fun generateCommentsForProducts(products: List<ProductEntity>): List<CommentEntity> {
        val rnd = Random()
        val comments = ArrayList<CommentEntity>()
        products.forEach { product ->
            val commentsNumber: Int = rnd.nextInt(5) + 1
            COMMENTS.take(commentsNumber).forEachIndexed { i, comment ->
                val commentEntity = CommentEntity(
                        null,
                        product.id,
                        "$comment for ${product.name}",
                        Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis((commentsNumber - i).toLong()) + TimeUnit.HOURS.toMillis(i.toLong())))
                comments.add(commentEntity)
            }
        }
        return comments
    }
}