package com.example.unsplash.ui.user_info

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
        if(result.resultCode == Activity.RESULT_OK) {
            viewModel.webLogoutComplete()
        } else {
            // логаут отменен
            // делаем complete тк github не редиректит после логаута и пользователь закрывает CCT
            viewModel.webLogoutComplete()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.corruptAccessToken.setOnClickListener {
            viewModel.corruptAccessToken()
        }
       binding.getUserInfo.setOnClickListener {
           viewModel.loadUserInfo()
       }

        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.loadingFlow.launchAndCollectIn(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.getUserInfo.isEnabled = !isLoading
            binding.userInfo.isVisible = !isLoading
        }

       viewModel.userInfoFlow.launchAndCollectIn(viewLifecycleOwner) { userInfo ->
          binding.userInfo.text = userInfo?.location
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