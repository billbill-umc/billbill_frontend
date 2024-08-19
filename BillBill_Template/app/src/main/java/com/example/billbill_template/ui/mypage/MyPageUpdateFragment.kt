package com.example.billbill_template.ui.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.billbill_template.Login.signin.LoginActivity
import com.example.billbill_template.Login.signup.RetrofitClient
import com.example.billbill_template.databinding.FragmentMyPageUpdateBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyPageUpdateFragment : Fragment() {

    private var _binding: FragmentMyPageUpdateBinding? = null
    private val binding get() = _binding!!
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageUpdateBinding.inflate(inflater, container, false)

        // 프로필 업데이트
        binding.mpClText.setOnClickListener {
            val username = binding.mpUdInputName.text.toString()
            val password = binding.mpUdInputPw.text.toString()
            val phoneNumber = binding.mpUdInputTp.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && phoneNumber.isNotEmpty()) {
                val profileRequest = ProfileRequest(username, phoneNumber)

                // 프로필 업데이트 API 호출
                RetrofitClient.instance.updateProfile(profileRequest).enqueue(object :
                    Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            saveUserInfo(username, phoneNumber, imageUri?.toString())
                            Toast.makeText(context, "프로필이 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                            parentFragmentManager.popBackStack()  // MyPageFragment로 돌아가기
                        } else {
                            Toast.makeText(context, "프로필 업데이트 실패.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "네트워크 오류 발생.", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 아바타 업로드
        binding.mpUserUdTv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // 로그아웃 클릭 리스너 추가
        binding.logoutTv.setOnClickListener {
            logout()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.mpUserUdIv.setImageURI(imageUri)

            // Glide를 사용하여 이미지를 동그랗게 로드
            imageUri?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.mpUserUdIv)

                // 아바타 업로드 API 호출
                val file = getFileFromUri(uri)
                file?.let {
                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), it)
                    val body = MultipartBody.Part.createFormData("avatar", it.name, requestFile)

                    // 아바타 업로드 API 호출
                    RetrofitClient.instance.uploadAvatar(body).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "아바타가 업로드되었습니다.", Toast.LENGTH_SHORT).show()
                                saveUserInfo(username = binding.mpUdInputName.text.toString(),
                                    phoneNumber = binding.mpUdInputTp.text.toString(),
                                    avatarUrl = uri.toString())
                                parentFragmentManager.popBackStack()  // MyPageFragment로 돌아가기
                            } else {
                                Toast.makeText(context, "아바타 업로드 실패.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(context, "네트워크 오류 발생.", Toast.LENGTH_SHORT).show()
                        }
                    })
                } ?: Toast.makeText(context, "파일 경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return filePath?.let { File(it) }
    }

    private fun saveUserInfo(username: String, phoneNumber: String, avatarUrl: String?) {
        val sharedPreferences = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USERNAME", username)
        editor.putString("PHONENUMBER", phoneNumber)
        editor.putString("AVATARURL", avatarUrl) // 아바타 URL도 저장
        editor.apply()  // 비동기로 저장
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 토큰 관련 정보만 삭제
        editor.remove("TOKEN")
        editor.remove("REFRESH_TOKEN") // 만약 Refresh Token이 있다면

        // 다른 프로필 정보 (USERNAME, AVATARURL 등)은 유지
        editor.apply()

        // Toast 메시지로 로그아웃 안내
        Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

        // LoginActivity로 화면 전환
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        // 현재 Fragment를 종료
        activity?.finish()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
