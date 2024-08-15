package com.example.billbill_template.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentHomeBinding
import com.example.billbill_template.post.OldVersionPost
import com.example.billbill_template.post.PostAddFragment
import com.example.billbill_template.post.PostFragment
import com.example.billbill_template.ui.search.NotificationFragment
import com.example.billbill_template.ui.search.SearchFragment
import com.google.gson.Gson

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val oldVersionPosts = ArrayList<OldVersionPost>()
    private val categorys = ArrayList<String>()

    private var categoryMoreVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.homeBarterButtonIv.setOnClickListener { // 물물교환 페이지 클릭 리스너
            navigateToHomeSwapFragment()
        }

        binding.homeSearchIv.setOnClickListener {
            navigateToSearchFragment() // 검색 페이지로 전환
        }

        binding.homeAlarmIv.setOnClickListener {
            navigateToNotificationFragment() // 알림 페이지로 전환
        }

        binding.homeAddButtonFb.setOnClickListener { //게시물 작성 페이지로 전환
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostAddFragment()).commitAllowingStateLoss()
        }

        binding.homeCategoryMoreIv.setOnClickListener { // 정렬 순서 더보기
            if(categoryMoreVisible) {
                binding.homeCategorySortLl.visibility = View.GONE
                categoryMoreVisible = false
            } else {
                binding.homeCategorySortLl.visibility = View.VISIBLE
                categoryMoreVisible = true
            }
        }


        //post
        oldVersionPosts.apply {
            add(OldVersionPost("사과", "원터치 텐트 (카즈미/A)", 2 , R.drawable.img_test_post_photo, "상세설명1", 30000, 10000, "서울", false, R.drawable.img_test_message_apple))
            add(OldVersionPost("오렌지", "도자기 컵", 3 , R.drawable.img_test_post_cup , "상세설명2", 15000, 30000, "대구", false, R.drawable.img_test_message_orange))
            add(OldVersionPost("파인애플", "IWC 손목시계", 1 , R.drawable.img_test_post_clock, "상세설명3", 24000, 1000, "부산", true, R.drawable.img_test_message_pineapple))
            add(OldVersionPost("오렌지", "Kodak Pony 828", 0 , R.drawable.img_test_post_camera , "상세설명2", 15000, 30000, "대구", false, R.drawable.img_test_message_orange))
        }


        val homePostRVAdapter = HomePostRVAdapter(oldVersionPosts)
        binding.homePostListRv.adapter = homePostRVAdapter
        binding.homePostListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        homePostRVAdapter.setPostItemClickListener(object  : HomePostRVAdapter.PostItemClickListener{
            override fun onItemClick(oldVersionPost: OldVersionPost) {
                changePostFragment(oldVersionPost)
            }
        })

        homePostRVAdapter.setPostItemClickListener(object  : HomePostRVAdapter.PostItemClickListener{
            override fun onItemClick(oldVersionPost: OldVersionPost) {
                changePostFragment(oldVersionPost)
            }
        })

        val bannerAdapter = HomeBannerVPAdapter(this)
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_test_home_banner))
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_test_home_banner))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL


//        //Indicator
        binding.homeBannerIndicator.setViewPager(binding.homeBannerVp)

//        //category
        categorys.apply {
            add("전체")
            add("캠핑")
            add("공구")
            add("스포츠")
            add("기타")
        }

        val homeCategoryRVAdapter = HomeCategoryRVAdapter(categorys)
        binding.homeCategoryRv.adapter = homeCategoryRVAdapter
        binding.homeCategoryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        homeCategoryRVAdapter.setHomeCategoryClickListener(object : HomeCategoryRVAdapter.HomeCategoryItemClickListener{
            override fun onItemClick(name: String) {
            }
        })

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }


        return root
    }

    private fun navigateToHomeSwapFragment() { // 물물교환 페이지로 이동하는 함수
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeSwapFragment())
            .addToBackStack(null) // 백스택에 추가하여 뒤로 가기 버튼으로 돌아올 수 있도록 함
            .commit()
    }

    private fun navigateToSearchFragment() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, SearchFragment()) // R.id.container는 Fragment를 담고 있는 FrameLayout의 ID입니다.
            .addToBackStack(null) // 뒤로 가기 버튼을 누르면 이전 Fragment로 돌아올 수 있도록 백스택에 추가
            .commit()
    }

    private fun navigateToNotificationFragment() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, NotificationFragment()) // R.id.container는 Fragment를 담고 있는 FrameLayout의 ID입니다.
            .addToBackStack(null) // 뒤로 가기 버튼을 누르면 이전 Fragment로 돌아올 수 있도록 백스택에 추가
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changePostFragment(oldVersionPost: OldVersionPost) {

//        val intent = Intent(context, PostFragment::class.java)
//        intent.putExtra("post",oldVersionPost.itemName)
//        //구현할 때에는 postId 값 넣기

        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, PostFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val postJson = gson.toJson(oldVersionPost)
                    putString("post", postJson)
                }
            })
            .addToBackStack(null) // 백스택에 추가하여 뒤로 가기 버튼으로 돌아올 수 있도록 함
            .commitAllowingStateLoss()
    }
}
