package com.example.billbill_template

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전체화면 모드 설정
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        setContentView(R.layout.activity_splash)

        // 3초 딜레이 후 LoginActivity 시작
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, com.example.billbill_template.ui.login.LoginActivity::class.java))
            finish()
        }, 3000)
    }
}
