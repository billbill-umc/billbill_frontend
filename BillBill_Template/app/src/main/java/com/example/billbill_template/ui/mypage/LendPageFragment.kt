package com.example.billbill_template.ui.lend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.billbill_template.databinding.FragmentLendPageBinding
import com.example.billbill_template.MainActivity

class LendPageFragment : Fragment() {

    private var _binding: FragmentLendPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLendPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // BottomNavigationView 숨기기
        (activity as? MainActivity)?.hideBottomNavigation()

        // lp_notification_back 클릭 리스너 설정
        binding.lpNotificationBack.setOnClickListener {
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
