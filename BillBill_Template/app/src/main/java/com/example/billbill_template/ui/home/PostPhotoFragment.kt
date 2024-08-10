package com.example.billbill_template.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.billbill_template.databinding.FragmentPostPhotoBinding

class PostPhotoFragment(val imgRes : Int): Fragment() {
    lateinit var binding : FragmentPostPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostPhotoBinding.inflate(inflater, container, false)

        binding.postPhotoIv.setImageResource(imgRes)
        return binding.root
    }
}