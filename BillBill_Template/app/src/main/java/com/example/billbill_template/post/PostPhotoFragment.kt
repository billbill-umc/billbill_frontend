package com.example.billbill_template.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.billbill_template.databinding.FragmentPostPhotoBinding

class PostPhotoFragment(val imageUrl: String): Fragment() {
    lateinit var binding : FragmentPostPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostPhotoBinding.inflate(inflater, container, false)

        // Glide를 사용해 이미지를 로드합니다.
        Glide.with(this)
            .load(imageUrl)
            .into(binding.postPhotoIv)

        return binding.root
    }
}
