package com.example.unsplash.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.example.unsplash.R
import com.example.unsplash.databinding.FragmentBottomNavigationBinding
import com.example.unsplash.ui.gallery.GalleryFragment
import com.example.unsplash.ui.user_info.UserInfoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavigationFragment : Fragment() {

    private var _binding: FragmentBottomNavigationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomNavigationBinding.inflate(inflater)

        binding.bottomView.setOnItemSelectedListener {
            val fragment = when (it.title) {
                "1" -> GalleryFragment()
                "2" -> UserInfoFragment()
                "3" -> UserInfoFragment()
                else -> return@setOnItemSelectedListener false
            }
            parentFragmentManager

            parentFragmentManager.commit {
                replace(R.id.container, fragment)
            }
            return@setOnItemSelectedListener true
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}