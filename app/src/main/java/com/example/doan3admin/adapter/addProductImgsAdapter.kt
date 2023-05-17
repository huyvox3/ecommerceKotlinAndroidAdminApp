package com.example.doan3admin.adapter
import android.view.View
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan3admin.R
import com.example.doan3admin.databinding.ImgItemBinding
import com.example.doan3admin.model.addProductModel

class addProductImgsAdapter(val list:ArrayList<Uri>):RecyclerView.Adapter<addProductImgsAdapter.addProductImageViewHolder>()
    {
    inner class addProductImageViewHolder(val binding: ImgItemBinding):RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): addProductImageViewHolder {
            val binding = ImgItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return  addProductImageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: addProductImageViewHolder, position: Int) {
            Glide.with(holder.itemView.context)
                .load(list[position])
                .into(holder.binding.itemImg)

        }
        override fun getItemCount(): Int {
            return list.size
        }

}