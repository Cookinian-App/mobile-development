package com.bangkitcapstone.cookinian.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.databinding.ItemHomeBannerBinding
import com.bumptech.glide.Glide

class HomeBannerAdapter(private val imageResId: List<Int>) :
    RecyclerView.Adapter<HomeBannerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResId: Int) {
            binding.ivItemHomeBanner.setImageResource(imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeBannerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageResId[position])
    }

    override fun getItemCount(): Int = imageResId.size
}
