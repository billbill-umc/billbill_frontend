package com.example.billbill_template.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentPostAddBinding

class PostAddFragment : Fragment() {
    private var _binding : FragmentPostAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostAddBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // BottomNavigationView 숨기기
        (activity as? MainActivity)?.hideBottomNavigation()

        binding.postAddBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commitAllowingStateLoss()
        }

//        binding.postAddPhotoRv.setOnClickListener {  }
//        binding.postAddButtonTv.setOnClickListener{}
//        binding.postAddInputCalendarIv.setOnClickListener{}

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // BottomNavigationView 다시 보이기
        (activity as? MainActivity)?.showBottomNavigation()
        _binding = null
    }
}