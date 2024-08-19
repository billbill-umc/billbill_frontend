package com.example.billbill_template.ui.mypage

data class EmailRequest(
    val email: String
)

data class PasswordRequest(
    val newPassword: String
)

data class ProfileRequest(
    val username: String,
    val phoneNumber: String
)

data class UserInfoResponse(
    val username: String,
    val avatarUrl: String
)
