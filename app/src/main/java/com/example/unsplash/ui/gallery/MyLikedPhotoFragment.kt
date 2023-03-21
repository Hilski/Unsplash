package com.example.unsplash.ui.gallery

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.unsplash.R
import com.example.unsplash.data.models.MyLikedPhoto
import com.example.unsplash.databinding.FragmentMyLikedPhotoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyLikedPhotoFragment : Fragment(R.layout.fragment_my_liked_photo),
    MyLikedPhotosAdapter.OnItemClickListener {

    private val viewModel by viewModels<MyLikedPhotosViewModel>()
    private var _binding: FragmentMyLikedPhotoBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMyLikedPhotoBinding.bind(view)
        arguments?.getString("usermame")?.let { viewModel.myLikedPhotoUsername(it) }
        val adapter = MyLikedPhotosAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() },
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.collections.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                //Пустое view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
    }

    override fun onItemClick(photo: MyLikedPhoto) {
        findNavController().navigate(
            R.id.action_myLikedPhotoFragment_to_collectionDetailsPhotoFragment,
            bundleOf("idPhoto" to photo.id)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}