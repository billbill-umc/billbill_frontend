package com.example.billbill_template

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.billbill_template.databinding.ActivityMainBinding
import com.example.billbill_template.ui.home.HomeFragment
import com.example.billbill_template.ui.message.MessageFragment
import com.example.billbill_template.ui.mypage.MyPageFragment
import com.example.billbill_template.Login.signin.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상단바를 숨깁니다
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences에서 저장된 토큰 가져오기
        val sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)

        // 토큰이 없는 경우 로그인 화면으로 이동
        if (token == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // MainActivity를 종료하여 뒤로가기 버튼으로 돌아오지 않도록 함
            return
        }

        // 하단 내비게이션 설정
        val navView: BottomNavigationView = binding.navView

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_message -> {
                    loadFragment(MessageFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_my_page -> {
                    loadFragment(MyPageFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        // 기본 프래그먼트를 로드합니다.
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }

    fun hideBottomNavigation() {
        binding.navView.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.navView.visibility = View.VISIBLE
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
