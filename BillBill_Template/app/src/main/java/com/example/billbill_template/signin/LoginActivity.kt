package com.example.billbill_template.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.billbill_template.MainActivity
import com.example.billbill_template.databinding.FragmentLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상단바를 숨깁니다
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        // FragmentLoginBinding 사용하여 레이아웃 설정
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 클릭 리스너 설정
        binding.loginBtnLogin.setOnClickListener {
            // MainActivity로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // 현재 Activity를 종료하여 뒤로 가기 버튼을 눌러도 LoginActivity로 돌아오지 않도록 함
        }
    }
}