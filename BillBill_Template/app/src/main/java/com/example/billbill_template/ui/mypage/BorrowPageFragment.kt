package com.example.billbill_template.ui.borrow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.billbill_template.MainActivity
import com.example.billbill_template.databinding.FragmentBorrowPageBinding

class BorrowPageFragment : Fragment() {

    private var _binding: FragmentBorrowPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBorrowPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // BottomNavigationView 숨기기
        (activity as? MainActivity)?.hideBottomNavigation()

        // bp_notification_back 클릭 리스너 설정
        binding.bpNotificationBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // BottomNavigationView 다시 보이기
        (activity as? MainActivity)?.showBottomNavigation()
        _binding = null
    }
}
