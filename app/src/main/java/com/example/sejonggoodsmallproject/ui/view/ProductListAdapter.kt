package com.example.sejonggoodsmallproject.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sejonggoodsmallproject.data.model.ProductListResponse
import com.example.sejonggoodsmallproject.databinding.ItemProductListBinding

class ProductListAdapter(private val list : List<ProductListResponse>)
    : RecyclerView.Adapter<ProductListAdapter.CustomViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener

    inner class CustomViewHolder(private val binding: ItemProductListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductListResponse) {
            binding.model = item
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = ItemProductListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CustomViewHolder(view)
    }

    override fun getItemCount()= list.size
}