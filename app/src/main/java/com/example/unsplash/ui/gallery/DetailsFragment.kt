package com.example.unsplash.ui.gallery

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.example.unsplash.R
import com.example.unsplash.databinding.FragmentDetailsBinding
import com.example.unsplash.ui.NavigationActivity
import com.example.unsplash.utils.launchAndCollectIn
import com.example.unsplash.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel by viewModels<DetailsViewModel>()
    private val args by navArgs<DetailsFragmentArgs>()
    private var photoLiked: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        viewModel.likePhotoInfo(args.photo.id)
        viewModel.likeItInfoFlow.launchAndCollectIn(viewLifecycleOwner) { likePhotoInfo ->
            if (likePhotoInfo != null) {
                binding.textViewLike.isVisible = likePhotoInfo.liked_by_user
                photoLiked = likePhotoInfo.liked_by_user
            }
        }

        viewModel.likeItFlow.launchAndCollectIn(viewLifecycleOwner) { likedPhoto ->
            if (likedPhoto != null) {
                binding.textViewLike.isVisible = likedPhoto.photo.liked_by_user
                photoLiked = likedPhoto.photo.liked_by_user
            }
        }

        viewModel.toastFlow.launchAndCollectIn(viewLifecycleOwner) {
            toast(it)
        }

        binding.apply {
            val photo = args.photo
            Glide.with(this@DetailsFragment)
                //full очень большая для показа прогресса загрузки, regular меньше
                .load(photo.urls.regular)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewCreator.isVisible = true
                        textViewDescription.isVisible = photo.description != null

                        return false
                    }
                })
                .into(imageView)

            textViewDescription.text = photo.description
            textViewLikesData.text = photo.likes

            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            textViewCreator.apply {
                text = "Photo by ${photo.user.name} on Unsplash"
                setOnClickListener {
                    context.startActivity(intent)
                }
                paint.isUnderlineText = true
            }

            likeItButton.setOnClickListener {
                if (photoLiked) {
                    viewModel.unlikePhoto(photo.id)
                } else {
                    viewModel.likePhoto(photo.id)
                }
            }

            //Сохраняем изображение в галлерею
            saveItButton.setOnClickListener {
                val fileName = photo.urls.full.substring(photo.urls.full.lastIndexOf('/') + 1)

                Glide.with(this@DetailsFragment)
                    .load(photo.urls.full)
                    .into(object : CustomTarget<Drawable?>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: com.bumptech.glide.request.transition.Transition<in Drawable?>?
                        ) {
                            val bitmap = (resource as BitmapDrawable).bitmap
                            Toast.makeText(context, "Saving Image...", Toast.LENGTH_SHORT)
                                .show()
                            //Сохраняем в галлерею
                            saveImageToGallery(bitmap, fileName)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            Toast.makeText(
                                context,
                                "Failed! Please try again later.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }
    fun saveImageToGallery(bitmap: Bitmap, filename: String) {
        val d = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            bitmap,
            filename,
            "yourDescription"
        );
        Toast.makeText(context, d, Toast.LENGTH_SHORT).show()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as NavigationActivity).supportActionBar?.hide()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}