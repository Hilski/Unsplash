package com.example.unsplash.ui.user_info

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.unsplash.R
import com.example.unsplash.databinding.FragmentUserInfoBinding
import com.example.unsplash.utils.launchAndCollectIn
import com.example.unsplash.utils.resetNavGraph
import com.example.unsplash.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

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


            val mBuilder = AlertDialog.Builder(requireContext())
                .setTitle("Confirm")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .show()

            // Function for the positive button
            // is programmed to exit the application
            val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
            mPositiveButton.setOnClickListener {
                viewModel.logout()
                exitProcess(0)
            }
        }

        viewModel.loadingFlow.launchAndCollectIn(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.userInfoFlow.launchAndCollectIn(viewLifecycleOwner) { userInfo ->
            if (userInfo != null) {
                binding.apply {
                    userIdData.text = userInfo.id
                    userNameData.text = userInfo.username
                    userEmailData.text = userInfo.email
                    userTotalLikesData.text = userInfo.total_likes

                    Glide.with(this@UserInfoFragment)
                        .load(userInfo.profile_image.large)
                        .error(R.drawable.ic_error)
                        .into(imageViewProfile)
                }
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