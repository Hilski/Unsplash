package com.example.unsplash.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.unsplash.R
import com.example.unsplash.data.models.CollectionsPhoto
import com.example.unsplash.databinding.ItemColectionsBinding
import com.example.unsplash.databinding.ItemUnsplashPhotoBinding


class CollectionsPhotoAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<CollectionsPhoto, CollectionsPhotoAdapter.CollectionsViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        val binding =
            ItemColectionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class CollectionsViewHolder(private val binding: ItemColectionsBinding) :
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

        fun bind(collections: CollectionsPhoto) {
            binding.apply {
                Glide.with(itemView)
                    .load(collections.cover_photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                    Glide.with(imageViewMyLike)
                        .load(R.drawable.photo_library)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageViewMyLike)

                textViewUserName.text = collections.user.username
                textViewTotalPhotos.text = collections.total_photos.toString()
                textViewTitle.text = collections.title
                textViewDescription.text = collections.description
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(collections: CollectionsPhoto)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<CollectionsPhoto>() {
            override fun areItemsTheSame(oldItem: CollectionsPhoto, newItem: CollectionsPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CollectionsPhoto,
                newItem: CollectionsPhoto
            ) = oldItem == newItem
        }
    }
}