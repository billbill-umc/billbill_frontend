package com.example.billbill_template.ui.home

import android.os.Bundle
import android.util.Log
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
import com.example.billbill_template.post.GetCategoryManifestResponse
import com.example.billbill_template.post.GetPostsData
import com.example.billbill_template.post.PostAddFragment
import com.example.billbill_template.post.PostFragment
import com.example.billbill_template.ui.search.NotificationFragment
import com.example.billbill_template.ui.search.SearchFragment

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
            navigateToPostAddFragment()
        }

        binding.homeCategoryMoreIv.setOnClickListener {
            toggleCategoryVisibility()
        }

        // RecyclerView 초기 설정
        setupRecyclerViews()

        val bannerAdapter = HomeBannerVPAdapter(this)
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_test_home_banner))
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_test_home_banner))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL


//        //Indicator
        binding.homeBannerIndicator.setViewPager(binding.homeBannerVp)

        return root
    }

    private fun setupRecyclerViews() {
        binding.homePostListRv.layoutManager = LinearLayoutManager(context)
        getpostsAdapter = HomePostRVAdapter(GetPostsData(emptyList()))
        binding.homePostListRv.adapter = getpostsAdapter


        binding.homeCategoryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeCategoryAdapter = HomeCategoryRVAdapter(GetCategoryManifestResponse(emptyList()), this)
        binding.homeCategoryRv.adapter = homeCategoryAdapter
    }

    private fun toggleCategoryVisibility() {
        if(categoryMoreVisible) {
            binding.homeCategorySortLl.visibility = View.GONE
            categoryMoreVisible = false
        } else {
            binding.homeCategorySortLl.visibility = View.VISIBLE
            categoryMoreVisible = true
        }
    }

    private fun navigateToHomeSwapFragment() { // 물물교환 페이지로 이동하는 함수
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeSwapFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToSearchFragment() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, SearchFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToNotificationFragment() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, NotificationFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToPostAddFragment() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, PostAddFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        refreshData()
    }

    private fun refreshData() {
        val homeService = HomeService()
        homeService.setHomeView(this)
        homeService.getPosts(requireContext(), -1)  // 이 메서드가 다시 호출되면 데이터를 새로 가져오게 됩니다.
        homeService.getCategories(requireContext())
    }

    private fun changePostFragment(postId: Int) {

        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, PostFragment().apply {
                arguments = Bundle().apply {
                    putInt("postId", postId)
                }
            })
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun onGetPostsSuccess(result: GetPostsData) {
        Log.d("HomeFragment", "onGetPostsSuccess called with ${result.posts.size} posts")

        getpostsAdapter.updateData(result)

        getpostsAdapter.setPostItemClickListener(object : HomePostRVAdapter.PostItemClickListener {
            override fun onItemClick(id: Int) {
                changePostFragment(id)
                Log.d("HomeFragment", "click item id : $id")
            }
        })

        Log.d("HomeFragment", "Get Posts Success")
    }

    override fun onGetPostsFailure(message: String) {
        Log.d("HomeFragment", "Get Posts Failure - ${message}")
    }

    override fun onGetCategoriesSuccess(result: GetCategoryManifestResponse) {
        homeCategoryAdapter.updateData(result)
        homeCategoryAdapter.setHomeCategoryClickListener(object : HomeCategoryRVAdapter.HomeCategoryItemClickListener {
            override fun onItemClick(name: String) {
                // 필요한 작업 추가
            }
        })
        Log.d("HomeFragment", "Get Categories Success")
    }

    override fun onGetCategoriesFailure(message: String) {
        Log.d("HomeFragment", "Get Categories Failure - ${message}")
    }
}
