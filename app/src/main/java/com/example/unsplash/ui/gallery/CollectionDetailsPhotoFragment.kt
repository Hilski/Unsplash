package com.example.unsplash.ui.gallery

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.example.unsplash.R
import com.example.unsplash.data.models.CollectionDetailsPhoto
import com.example.unsplash.databinding.FragmentCollectionDetailsPhotoBinding
import com.example.unsplash.utils.launchAndCollectIn
import com.example.unsplash.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionDetailsPhotoFragment : Fragment() {

    private val viewModel by viewModels<CollectionDetailsPhotoViewModel>()

    private var _binding: FragmentCollectionDetailsPhotoBinding? = null
    private val binding get() = _binding!!
    private var photoLiked: Boolean = false
    private var getPhoto : CollectionDetailsPhoto? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectionDetailsPhotoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPhoto: String = arguments?.getString("idPhoto") ?: ""

        viewModel.likePhotoInfo(idPhoto)
        viewModel.getPhoto(idPhoto)

        viewModel.getPhotoFlow.launchAndCollectIn(viewLifecycleOwner) { photo ->
            if (photo != null) {
                getPhoto = photo
                loadImage()
            }
        }

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

        binding.likeItButton.setOnClickListener {
            if (photoLiked) {
                viewModel.unlikePhoto(idPhoto)
            } else {
                viewModel.likePhoto(idPhoto)
            }
        }

        //Сохраняем изображение в галлерею
        binding.saveItButton.setOnClickListener {
            val fileName = getPhoto?.urls?.regular?.substring((getPhoto?.urls?.regular?.lastIndexOf('/')
                ?: 0) + 1)

            Glide.with(this@CollectionDetailsPhotoFragment)
                .load(getPhoto?.urls?.regular)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in Drawable?>?
                    ) {
                        val bitmap = (resource as BitmapDrawable).bitmap
                        Toast.makeText(context, "Saving Image...", Toast.LENGTH_SHORT)
                            .show()
                        //Сохраняем в галлерею
                        if (fileName != null) {
                            saveImageToGallery(bitmap, fileName)
                        }
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

    private fun loadImage() {
        binding.apply {
            Glide.with(this@CollectionDetailsPhotoFragment)
                //full очень большая для показа прогресса загрузки, regular меньше
                .load(getPhoto!!.urls.regular)
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
                        return false
                    }
                })
                .into(imageView)

            textViewDescription.text = getPhoto?.description ?: ""
            textViewLikesData.text = getPhoto?.likes.toString()
        }
    }

    fun saveImageToGallery(bitmap: Bitmap, filename: String) {
        val d = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            bitmap,
            filename,
            "yourDescription"
        )
        Toast.makeText(context, d, Toast.LENGTH_SHORT).show()
    }
}