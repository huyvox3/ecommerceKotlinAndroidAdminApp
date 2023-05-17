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
import com.example.doan3admin.model.categoryModel



class categoryAdapter( private val items: List<categoryModel>) :
    RecyclerView.Adapter<categoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_tv)

        val imageView: ImageView = view.findViewById(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_layout, parent, false)
        Log.e("RecyclerView", "ok")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        Log.e("RecyclerView", "ok")
        Glide.with(holder.imageView.context).load(item.image).into(holder.imageView)

    }

    override fun getItemCount() = items.size
}
