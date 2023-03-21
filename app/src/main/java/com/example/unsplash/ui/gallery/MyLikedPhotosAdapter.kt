package com.example.unsplash.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.unsplash.R
import com.example.unsplash.data.models.MyLikedPhoto
import com.example.unsplash.databinding.ItemMyLikedPhotoBinding

class MyLikedPhotosAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<MyLikedPhoto, MyLikedPhotosAdapter.MyLikedViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyLikedViewHolder {
        val binding =
            ItemMyLikedPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyLikedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyLikedViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class MyLikedViewHolder(private val binding: ItemMyLikedPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(photo: MyLikedPhoto) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                if (photo.liked_by_user) {
                    Glide.with(imageViewMyLike)
                        .load(R.drawable.ic_favorite)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageViewMyLike)
                } else {
                    Glide.with(imageViewMyLike)
                        .load(R.drawable.ic_favorite_border)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageViewMyLike)
                }
                textViewUserName.text = photo.user.username
                textViewLikes.text = photo.likes.toString()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo: MyLikedPhoto)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<MyLikedPhoto>() {
            override fun areItemsTheSame(oldItem: MyLikedPhoto, newItem: MyLikedPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MyLikedPhoto,
                newItem: MyLikedPhoto
            ) = oldItem == newItem
        }
    }
}