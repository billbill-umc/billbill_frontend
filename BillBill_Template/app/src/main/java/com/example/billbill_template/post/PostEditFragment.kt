package com.example.billbill_template.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentPostEditBinding

class PostEditFragment : Fragment() {

    private var _binding : FragmentPostEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostEditBinding.inflate(inflater, container, false)
        val root : View = binding.root

        // BottomNavigationView 숨기기
        (activity as? MainActivity)?.hideBottomNavigation()

        binding.postEditBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostFragment()).commitAllowingStateLoss()
        }

//        binding.postAddPhotoRv.setOnClickListener {  }
//        binding.postAddButtonTv.setOnClickListener{}
//        binding.postAddInputCalendarIv.setOnClickListener{}

        return root
    }

}