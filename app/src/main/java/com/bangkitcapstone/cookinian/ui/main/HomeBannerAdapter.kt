package com.bangkitcapstone.cookinian.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.databinding.ItemHomeBannerBinding
import com.bumptech.glide.Glide

class HomeBannerAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<HomeBannerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeBannerBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = imageUrls.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.binding.ivItemHomeBanner.context)
            .load(imageUrl)
            .into(holder.binding.ivItemHomeBanner)
    }
}
