package com.example.billbill_template.Login.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.billbill_template.Login.signin.LoginActivity
import com.example.billbill_template.databinding.ActivitySignupAddBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.StandardCharsets

class SignUpEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상단바를 숨깁니다
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        // 전달된 이메일과 stateCode를 받습니다.
        val email = intent.getStringExtra("email")
        val stateCode = intent.getStringExtra("stateCode")

        // ActivitySignupAddBinding 사용하여 레이아웃 설정
        binding = ActivitySignupAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 기본 전화번호를 설정합니다.
        binding.signupInputPn.setText("010")

        // 닉네임 중복 확인 버튼 클릭 시
        binding.signupIdCheck.setOnClickListener {
            val username = binding.signupInputId.text.toString()

            if (username.isNotEmpty()) {
                showToast("사용 가능한 닉네임입니다.")
            } else {
                showToast("닉네임을 입력해주세요.")
            }
        }

        // 회원가입 버튼 클릭 시
        binding.signupBtnSu.setOnClickListener {
            val username = binding.signupInputId.text.toString()
            val password = binding.signupInputPw.text.toString()
            val phone = binding.signupInputPn.text.toString()
            val email = intent.getStringExtra("email") ?: ""

            if (username.isNotEmpty() && validatePassword(password) && validatePhoneNumber(phone)) {
                val signUpRequest = SignUpRequest(username, password, phone, email)
                RetrofitClient.instance.signUp(signUpRequest).enqueue(object : Callback<SignUpResponse> {
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            showToast("회원가입이 완료되었습니다.")
                            // LoginActivity로 이동
                            val intent = Intent(this@SignUpEmailActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish() // 현재 Activity 종료하여 뒤로 가기 버튼을 눌러도 돌아오지 않도록
                        } else {
                            showToast("회원가입에 실패했습니다.")
                        }
                    }

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        showToast("네트워크 오류가 발생했습니다.")
                    }
                })
            } else {
                showToast("입력한 정보를 확인해주세요.")
            }
        }
    }

    // 전화번호 형식 검증 (하이픈 포함)
    private fun validatePhoneNumber(phone: String): Boolean {
        val cleanedPhone = phone.replace(Regex("[^\\d-]"), "") // 숫자와 하이픈만 남김
        Log.d("Validation", "Entered Phone: $cleanedPhone")
        val isValid = cleanedPhone.matches(Regex("^010-\\d{4}-\\d{4}$"))
        Log.d("Validation", "Phone number valid: $isValid")
        return isValid
    }

    // 비밀번호 검증 (복잡도 증가)
    private fun validatePassword(password: String): Boolean {
        // 서버의 요구사항에 맞게 정규식 수정 필요
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{12,}$"
        val isValid = password.matches(passwordPattern.toRegex())
        Log.d("Validation", "Password valid: $isValid")
        return isValid
    }

    // 깨진 문자를 방지하기 위해 UTF-8로 인코딩된 문자열로 Toast 메시지를 표시합니다.
    private fun showToast(message: String) {
        val encodedMessage = String(message.toByteArray(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
        Toast.makeText(this, encodedMessage, Toast.LENGTH_SHORT).show()
    }
}
