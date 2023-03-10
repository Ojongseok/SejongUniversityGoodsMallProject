package com.example.sejonggoodsmallproject.ui.view.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sejonggoodsmallproject.data.model.OrderListItems
import com.example.sejonggoodsmallproject.data.model.OrderListResponse
import com.example.sejonggoodsmallproject.databinding.ItemOrderCompleteList2Binding
import com.example.sejonggoodsmallproject.ui.viewmodel.MainViewModel
import com.example.sejonggoodsmallproject.ui.viewmodel.ProductDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderCompleteListAdapter2(
    private val context: Context,
    private val response: List<OrderListItems>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<OrderCompleteListAdapter2.CustomViewHolder>() {
    inner class CustomViewHolder(private val binding: ItemOrderCompleteList2Binding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderListItems) {
            CoroutineScope(Dispatchers.IO).launch {
                val result = viewModel.getProductDetail(item.itemId.toInt()).body()!!

                withContext(Dispatchers.Main) {
                    binding.tvItemOrderCompleteTitle.text = result.title
                    Glide.with(context).load(result.img[0].oriImgName).into(binding.ivProduct)

                    binding.tvItemOrderCompleteSeller.text = item.seller.name
                    binding.tvItemOrderCompleteStatus.text // 이거 api수정해야ㅕ하지않나
                    binding.tvItemOrderCompleteStatus.text
                    binding.tvItemOrderCompleteQuantity.text = "수량 ${item.quantity}개"
                    binding.tvItemOrderCompletePrice.text = priceUpdate(item.price)
                    binding.tvItemOrderCompleteOption.text = if (item.color != null && item.size != null) {
                        "${item.color}, ${item.size}"
                    } else if (item.color != null && item.size == null) {
                        "${item.color}"
                    } else if (item.color == null && item.size != null) {
                        "${item.size}"
                    } else if (item.color == null && item.size == null) {
                        "선택사항 없음"
                    } else { "" }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = ItemOrderCompleteList2Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(response[position])
    }

    fun priceUpdate(price: Int) : String {
        if (price in 1000..999999) {
            val priceList = price.toString().toCharArray().toMutableList()
            priceList.add(priceList.size-3,',')
            return priceList.joinToString("") + "원"
        } else {
            return price.toString() + "원"
        }
    }
    override fun getItemCount() = response.size
}