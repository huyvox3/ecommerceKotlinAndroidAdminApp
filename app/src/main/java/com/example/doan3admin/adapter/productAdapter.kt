package com.example.doan3admin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan3admin.R
import com.example.doan3admin.databinding.ImgItemBinding
import com.example.doan3admin.databinding.ItemProductLayoutBinding
import com.example.doan3admin.model.addProductModel

class productAdapter( val items: List<addProductModel> ):RecyclerView.Adapter<productAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_tv)
        val price_tv:TextView = view.findViewById(R.id.price_tv)
        val qt_tv:TextView = view.findViewById(R.id.quantity_tv)
        val imageView: ImageView = view.findViewById(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_layout, parent, false)
        Log.e("RecyclerView", "ok")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.productName
        Log.e("RecyclerView", "ok")
        Glide.with(holder.itemView.context).load(item.productCoverImage).into(holder.imageView)
        holder.qt_tv.text = item.productQt
        holder.price_tv.text = item.productP

    }

    override fun getItemCount() = items.size

}