package com.rale.mvvmroom.ui

import android.support.v7.util.DiffUtil
import android.support.v7.util.DiffUtil.calculateDiff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rale.mvvmroom.R
import com.rale.mvvmroom.db.entity.CommentEntity
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private var comments = mutableListOf<CommentEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = if (comments != null) comments!!.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = comments[position]
        holder.bind(entity)
    }

    fun setComments(newComments: List<CommentEntity>) {
        if (comments.isEmpty()) {
            comments.addAll(newComments)
            notifyItemRangeInserted(0, newComments.size)
        } else {
            val oldComments = comments.toList()
            val diffResult = calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldId = oldComments[oldItemPosition].id
                    val newId = newComments[newItemPosition].id
                    return oldId == newId
                }

                override fun getOldListSize() = oldComments.size

                override fun getNewListSize() = newComments.size

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                        oldComments[oldItemPosition] == newComments[newItemPosition]
            })
            comments.clear()
            comments.addAll(newComments)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(comment: CommentEntity) {
            with(comment) {
                itemView.commentDesc.text = text
            }
        }
    }
}