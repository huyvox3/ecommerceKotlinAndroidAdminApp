package com.example.doan3admin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan3admin.databinding.ImgItemBinding
import com.example.doan3admin.model.sliderModel

class sliderAdapter (private val list: ArrayList<Uri>, private val context: Context):RecyclerView.Adapter<sliderAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ImgItemBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImgItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(list[position])
            .into(holder.binding.itemImg)
    }

    override fun getItemCount(): Int = list.size
}