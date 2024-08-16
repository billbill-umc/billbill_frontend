package com.example.billbill_template.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.billbill_template.MainActivity
import com.example.billbill_template.R
import com.example.billbill_template.databinding.FragmentHomeBinding
import com.example.billbill_template.post.GetCategoryManifestResponse
import com.example.billbill_template.post.GetPostsData
import com.example.billbill_template.post.PostAddFragment
import com.example.billbill_template.post.PostFragment
import com.example.billbill_template.ui.search.NotificationFragment
import com.example.billbill_template.ui.search.SearchFragment
import com.google.gson.Gson

class HomeFragment : Fragment(), HomeView {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var getpostsAdapter : HomePostRVAdapter
    private lateinit var homeCategoryAdapter : HomeCategoryRVAdapter
    //    private val categorys = ArrayList<String>()
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



//        val homePostRVAdapter = HomePostRVAdapter(posts)
//        binding.homePostListRv.adapter = homePostRVAdapter
//        binding.homePostListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        homePostRVAdapter.setPostItemClickListener(object  : HomePostRVAdapter.PostItemClickListener{
//            override fun onItemClick(post: GetPostsPosts) {
//                changePostFragment(post)
//            }
//        })
//
//        homePostRVAdapter.setPostItemClickListener(object  : HomePostRVAdapter.PostItemClickListener{
//            override fun onItemClick(oldVersionPost: GetPostsPosts) {
//                changePostFragment(oldVersionPost)
//            }
//        })

        val bannerAdapter = HomeBannerVPAdapter(this)
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_test_home_banner))
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_test_home_banner))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL


//        //Indicator
        binding.homeBannerIndicator.setViewPager(binding.homeBannerVp)

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

    override fun onStart() {
        super.onStart()
        val homeService = HomeService()
        homeService.setPostView(this)
        homeService.getPosts(requireContext())
        homeService.getCategories(requireContext())
    }

    private fun changePostFragment(postId: Int) {

        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, PostFragment().apply {
                arguments = Bundle().apply {
                    putInt("postId", postId)
                }
            })
            .addToBackStack(null) // 백스택에 추가하여 뒤로 가기 버튼으로 돌아올 수 있도록 함
            .commitAllowingStateLoss()
    }

    override fun onGetPostsSuccess(result: GetPostsData) {
        getpostsAdapter = HomePostRVAdapter(result)
        binding.homePostListRv.adapter = getpostsAdapter
        getpostsAdapter.setPostItemClickListener(object : HomePostRVAdapter.PostItemClickListener {
            override fun onItemClick(id: Int) {
                changePostFragment(id)
                Log.d("HomeFragment", "click item id : ${id}")
            }
        })
        Log.d("HomeFragment", "Get Posts Success")
    }

    override fun onGetPostsFailure(message: String) {
        Log.d("HomeFragment", "Get Posts failure - ${message}")
    }

    override fun onGetCategoriesSuccess(result: GetCategoryManifestResponse) {
        homeCategoryAdapter= HomeCategoryRVAdapter(result)
        binding.homeCategoryRv.adapter = homeCategoryAdapter
        homeCategoryAdapter.setHomeCategoryClickListener(object : HomeCategoryRVAdapter.HomeCategoryItemClickListener{
            override fun onItemClick(name: String) { }
        })
        Log.d("HomeFragment", "Get Categories Success")
    }

    override fun onGetCategoriesFailure(message: String) {
        Log.d("HomeFragment", "Get Categories failure - ${message}")
    }
}
