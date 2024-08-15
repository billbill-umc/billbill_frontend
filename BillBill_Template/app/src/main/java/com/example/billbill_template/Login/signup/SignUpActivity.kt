package com.example.billbill_template.Login.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.billbill_template.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상단바를 숨깁니다
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        // ActivitySignupBinding 사용하여 레이아웃 설정
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이메일 인증 버튼 클릭 시
        binding.signupBtnVc.setOnClickListener {
            val email = binding.signupInputEmail.text.toString()

            if (email.isNotEmpty() && isValidEmail(email)) {
                sendEmailVerification(email)
            } else {
                Toast.makeText(this, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 인증 코드 확인 버튼 클릭 시
        binding.signupBtnVcCode.setOnClickListener {
            val email = binding.signupInputEmail.text.toString()
            val code = binding.signupInputEmailCode.text.toString()

            if (email.isNotEmpty() && code.isNotEmpty()) {
                verifyEmailCode(email, code)
            } else {
                Toast.makeText(this, "이메일과 인증 코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 이메일 형식이 유효한지 확인
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    var stateCode: String? = null

    // 이메일 인증 요청 보내기
    private fun sendEmailVerification(email: String) {
        val emailRequest = EmailRequest(email)
        RetrofitClient.instance.sendEmailVerification(emailRequest).enqueue(object : Callback<EmailResponse> {
            override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                if (response.isSuccessful) {
                    val emailResponse = response.body()
                    if (emailResponse?.success == true) {
                        Log.d("MyApp", "이메일 인증 성공")
                        Toast.makeText(this@SignUpActivity, "이메일로 인증 코드가 발송되었습니다.", Toast.LENGTH_SHORT).show()
                        // 서버 응답에서 stateCode를 자동으로 추출하여 저장
                        stateCode = emailResponse.data?.stateCode
                        binding.signupInputEmailGroup.visibility = View.GONE
                        binding.signupInputCodeGroup.visibility = View.VISIBLE
                    } else {
                        val errorMessage = emailResponse?.message ?: "이메일 인증 실패"
                        Log.e("MyApp", "이메일 인증 실패 - 서버 응답: ${response.body()?.toString()}")
                        Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MyApp", "이메일 인증 실패 - 응답 코드: ${response.code()}, 응답 메시지: ${response.message()}")
                    Toast.makeText(this@SignUpActivity, "서버 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                Log.e("MyApp", "이메일 인증 실패 - 오류: ${t.message}")
                t.printStackTrace()
                Toast.makeText(this@SignUpActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 이메일 인증 코드 검증 요청 보내기
    private fun verifyEmailCode(email: String, code: String) {
        Log.d("MyApp", "Verifying with stateCode: $stateCode")

        if (stateCode != null) {
            val verificationRequest = EmailVerificationRequest(email, code, stateCode!!, code) // authCode를 code와 동일하게 설정
            RetrofitClient.instance.verifyEmail(verificationRequest).enqueue(object : Callback<VerificationResponse> {
                override fun onResponse(call: Call<VerificationResponse>, response: Response<VerificationResponse>) {
                    if (response.isSuccessful) {
                        val verificationResponse = response.body()
                        if (verificationResponse?.success == true) {
                            Log.d("MyApp", "이메일 인증 코드 확인 성공")
                            Toast.makeText(this@SignUpActivity, "이메일 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                            // SignUpEmailActivity로 이동
                            val intent = Intent(this@SignUpActivity, SignUpEmailActivity::class.java)
                            intent.putExtra("email", email) // 이메일을 다음 액티비티로 전달
                            intent.putExtra("stateCode", stateCode) // stateCode를 다음 액티비티로 전달
                            startActivity(intent)
                        } else {
                            val errorMessage = "인증 코드가 유효하지 않습니다. 다시 시도해주세요."
                            Log.e("MyApp", "이메일 인증 코드 확인 실패 - 서버 응답: ${response.body()?.toString()}")
                            Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("MyApp", "이메일 인증 코드 확인 실패 - 응답 코드: ${response.code()}, 응답 메시지: ${response.message()}")
                        Toast.makeText(this@SignUpActivity, "서버 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
                    Log.e("MyApp", "이메일 인증 코드 확인 실패 - 오류: ${t.message}")
                    t.printStackTrace()
                    Toast.makeText(this@SignUpActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.e("MyApp", "StateCode가 null입니다.")
            Toast.makeText(this, "인증을 위한 데이터가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
