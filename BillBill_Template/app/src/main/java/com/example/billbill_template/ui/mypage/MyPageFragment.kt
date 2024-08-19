package com.example.billbill_template.ui.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentMyPageBinding
import com.example.billbill_template.ui.lend.LendPageFragment
import com.example.billbill_template.ui.borrow.BorrowPageFragment

class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val myPageViewModel =
            ViewModelProvider(this).get(MyPageViewModel::class.java)

        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // mp_lend_layout 클릭 리스너 설정
        binding.mpLendLayout.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, LendPageFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // mp_borrow_layout 클릭 리스너 설정
        binding.mpBorrowLayout.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, BorrowPageFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // mp_swap_layout 클릭 리스너 설정
        binding.mpSwapLayout.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, SwapPageFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // btn_profile_change 클릭 리스너 설정
        binding.btnProfileChange.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, MyPageUpdateFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        // SharedPreferences에서 사용자 정보를 로드하여 UI에 반영
        loadUserInfoFromPreferences()
    }

    private fun loadUserInfoFromPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", "Unknown")
        val avatarUrl = sharedPreferences.getString("AVATARURL", null)

        // 디버그 로그 추가
        Log.d("MyPageFragment", "Loaded Username: $username, AvatarUrl: $avatarUrl")

        // 유저네임 업데이트
        binding.mpUnTv.text = username

        // 아바타 이미지 업데이트 (Glide를 사용하여 동그랗게 로드)
        if (avatarUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(R.drawable.img_test_home_user) // 기본 이미지 설정
                .apply(RequestOptions.circleCropTransform())
                .into(binding.mpUserIv)
        } else {
            Glide.with(this)
                .load(avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.mpUserIv)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
