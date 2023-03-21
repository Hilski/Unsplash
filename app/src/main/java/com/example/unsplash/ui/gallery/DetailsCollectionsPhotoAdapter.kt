package com.example.unsplash.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.unsplash.R
import com.example.unsplash.data.models.GetCollectionsPhoto
import com.example.unsplash.databinding.ItemUnsplashPhotoBinding

class DetailsCollectionsPhotoAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<GetCollectionsPhoto, DetailsCollectionsPhotoAdapter.DetailsCollectionsViewHolder>(
        PHOTO_COMPARATOR
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsCollectionsViewHolder {
        val binding =
            ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailsCollectionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailsCollectionsViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class DetailsCollectionsViewHolder(private val binding: ItemUnsplashPhotoBinding) :
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

        fun bind(collections: GetCollectionsPhoto) {
            binding.apply {
                Glide.with(itemView)
                    .load(collections.urls.small)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                if (collections.liked_by_user) {
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
                textViewUserName.text = collections.user.username
                textViewLikes.text = collections.likes.toString()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(collections: GetCollectionsPhoto)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<GetCollectionsPhoto>() {
            override fun areItemsTheSame(
                oldItem: GetCollectionsPhoto,
                newItem: GetCollectionsPhoto
            ) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: GetCollectionsPhoto,
                newItem: GetCollectionsPhoto
            ) = oldItem == newItem
        }
    }
}