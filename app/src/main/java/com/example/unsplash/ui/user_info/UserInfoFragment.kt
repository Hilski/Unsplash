package com.example.unsplash.ui.user_info

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.unsplash.R
import com.example.unsplash.databinding.FragmentUserInfoBinding
import com.example.unsplash.utils.launchAndCollectIn
import com.example.unsplash.utils.resetNavGraph
import com.example.unsplash.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private val viewModel: UserInfoViewModel by viewModels()
    private val binding by viewBinding(FragmentUserInfoBinding::bind)

    private val logoutResponse = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.webLogoutComplete()
        } else {
            // логаут отменен
            viewModel.webLogoutComplete()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadUserInfo()

        binding.myLikedPhoto.setOnClickListener {
            findNavController().navigate(
                R.id.action_userInfoFragment_to_myLikedPhotoFragment,
                bundleOf(
                    "usermame" to binding.userNameData.text
                )
            )
        }

        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.loadingFlow.launchAndCollectIn(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.userInfoFlow.launchAndCollectIn(viewLifecycleOwner) { userInfo ->
            if (userInfo != null) {
                binding.userIdData.text = userInfo.id
                binding.userNameData.text = userInfo.username
                binding.userEmailData.text = userInfo.email
                binding.userTotalLikesData.text = userInfo.total_likes
            }

        }

        viewModel.toastFlow.launchAndCollectIn(viewLifecycleOwner) {
            toast(it)
        }

        viewModel.logoutPageFlow.launchAndCollectIn(viewLifecycleOwner) {
            logoutResponse.launch(it)
        }

        viewModel.logoutCompletedFlow.launchAndCollectIn(viewLifecycleOwner) {
            findNavController().resetNavGraph(R.navigation.nav_graph)
        }
    }
}