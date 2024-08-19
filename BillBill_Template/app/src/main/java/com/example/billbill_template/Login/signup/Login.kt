package com.example.billbill_template.Login.signup

data class EmailRequest(val email: String)
data class EmailResponse(
    val success: Boolean,
    val message: String?,      // 실패 원인에 대한 메시지
    val errorCode: Int?,        // 에러 코드
    val data: StateAuthData? // 서버에서 반환하는 데이터를 담을 클래스
)

data class StateAuthData(
    val stateCode: String,
    val authCode: String
)


data class EmailVerificationRequest(
    val email: String,
    val code: String,
    val stateCode: String,  // 추가된 필드
    val authCode: String    // 추가된 필드
)

data class VerificationResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: Any?
)




data class SignUpRequest(
    val username: String,  // 변경된 필드명
    val password: String,
    val phoneNumber: String,
    val email: String
)



data class SignUpResponse(val success: Boolean)

data class LoginRequest(
    val email: String,
    val password: String
)


data class TokenResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: TokenData,
)

data class TokenData(
    val accessToken: String,
    val refreshToken: String,
    val username: String,
    val avatarUrl: String
)


data class RefreshTokenRequest(
    val refreshToken: String
)
