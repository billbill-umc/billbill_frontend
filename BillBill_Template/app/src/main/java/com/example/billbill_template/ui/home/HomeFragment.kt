package com.example.billbill_template.ui.home

import PostAddFragment
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
<<<<<<< HEAD
=======
import com.example.billbill_template.post.PostAddFragment
>>>>>>> origin/main
import com.example.billbill_template.post.PostFragment
import com.example.billbill_template.ui.search.NotificationFragment
import com.example.billbill_template.ui.search.SearchFragment
import com.google.gson.Gson

class HomeFragment : Fragment(), HomeView {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var getpostsAdapter : HomePostRVAdapter
    private lateinit var homeCategoryAdapter : HomeCategoryRVAdapter
<<<<<<< HEAD
=======
    //    private val categorys = ArrayList<String>()
>>>>>>> origin/main
    private var categoryMoreVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

<<<<<<< HEAD
        binding.homeBarterButtonIv.setOnClickListener {
=======

        binding.homeBarterButtonIv.setOnClickListener { // 물물교환 페이지 클릭 리스너
>>>>>>> origin/main
            navigateToHomeSwapFragment()
        }

        binding.homeSearchIv.setOnClickListener {
            navigateToSearchFragment()
        }

        binding.homeAlarmIv.setOnClickListener {
            navigateToNotificationFragment()
        }

<<<<<<< HEAD
        binding.homeAddButtonFb.setOnClickListener {
            navigateToPostAddFragment()
        }

        binding.homeCategoryMoreIv.setOnClickListener {
            toggleCategoryVisibility()
        }

        // RecyclerView 초기 설정
        setupRecyclerViews()
=======
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
>>>>>>> origin/main

        return root
    }

    private fun setupRecyclerViews() {
        binding.homePostListRv.layoutManager = LinearLayoutManager(context)
        getpostsAdapter = HomePostRVAdapter(GetPostsData(emptyList()))
        binding.homePostListRv.adapter = getpostsAdapter


        binding.homeCategoryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeCategoryAdapter = HomeCategoryRVAdapter(GetCategoryManifestResponse(emptyList()))
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

    private fun navigateToHomeSwapFragment() {
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
<<<<<<< HEAD
        refreshData()
    }
=======
        val homeService = HomeService()
        homeService.setPostView(this)
        homeService.getPosts(requireContext())
        homeService.getCategories(requireContext())
    }

    private fun changePostFragment(postId: Int) {
>>>>>>> origin/main

    private fun refreshData() {
        val homeService = HomeService()
        homeService.setPostView(this)
        homeService.getPosts(requireContext())  // 이 메서드가 다시 호출되면 데이터를 새로 가져오게 됩니다.
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
<<<<<<< HEAD
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
        Log.d("HomeFragment", "Get Posts failure - $message")
    }

    override fun onGetCategoriesSuccess(result: GetCategoryManifestResponse) {
        homeCategoryAdapter.updateData(result)
        homeCategoryAdapter.setHomeCategoryClickListener(object : HomeCategoryRVAdapter.HomeCategoryItemClickListener {
            override fun onItemClick(name: String) {
                // 필요한 작업 추가
            }
=======
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
>>>>>>> origin/main
        })
        Log.d("HomeFragment", "Get Categories Success")
    }

    override fun onGetCategoriesFailure(message: String) {
<<<<<<< HEAD
        Log.d("HomeFragment", "Get Categories failure - $message")
=======
        Log.d("HomeFragment", "Get Categories failure - ${message}")
>>>>>>> origin/main
    }
}
