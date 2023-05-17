package com.example.doan3admin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan3admin.activity.OrderDetailsActivity
import com.example.doan3admin.databinding.AllOrderItemLayoutBinding
import com.example.doan3admin.databinding.ItemOrderLineBinding
import com.example.doan3admin.model.OrderLineModel
import com.example.doan3admin.model.OrderModel
import com.google.firebase.firestore.FirebaseFirestore

class OrderlineAdapter(val list: ArrayList<OrderLineModel>, val context: Context): RecyclerView.Adapter<OrderlineAdapter.OrderLineAdapterViewHolder>(){
    inner class OrderLineAdapterViewHolder (val binding: ItemOrderLineBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderLineAdapterViewHolder {
        return  OrderLineAdapterViewHolder(
            ItemOrderLineBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: OrderLineAdapterViewHolder, position: Int) {

        holder.binding.productPriceTv.text = "Price: $"+ list[position].price

        holder.binding.productNameTv.text =  list[position].name

        holder.binding.quantityTv.text = list[position].quantity

        Glide.with(context).load(list[position].img).into(holder.binding.lineImgIv)


    }





    override fun getItemCount() = list.size


}