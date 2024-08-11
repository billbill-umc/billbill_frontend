package com.example.billbill_template.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
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
            // 프래그먼트 트랜잭션을 사용하여 프래그먼트 교체
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, LendPageFragment())
            transaction.addToBackStack(null) // 뒤로 가기 버튼으로 돌아올 수 있도록 백 스택에 추가
            transaction.commit()
        }

        // mp_borrow_layout 클릭 리스너 설정
        binding.mpBorrowLayout.setOnClickListener {
            // 프래그먼트 트랜잭션을 사용하여 프래그먼트 교체
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, BorrowPageFragment())
            transaction.addToBackStack(null) // 뒤로 가기 버튼으로 돌아올 수 있도록 백 스택에 추가
            transaction.commit()
        }

        // mp_borrow_layout 클릭 리스너 설정
        binding.mpSwapLayout.setOnClickListener {
            // 프래그먼트 트랜잭션을 사용하여 프래그먼트 교체
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, BorrowPageFragment())
            transaction.addToBackStack(null) // 뒤로 가기 버튼으로 돌아올 수 있도록 백 스택에 추가
            transaction.commit()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
