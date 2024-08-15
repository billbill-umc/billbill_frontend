package com.example.billbill_template.Login.signup

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/signin/email/send")
    fun sendEmailVerification(@Body email: EmailRequest): Call<EmailResponse>

    @POST("/signin/email/verify")
    fun verifyEmail(@Body email: EmailVerificationRequest): Call<VerificationResponse>

    @POST("/signin")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST("/auth/token")
    fun getToken(@Body loginRequest: LoginRequest): Call<TokenResponse>

    @POST("/auth/token/refresh")
    fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Call<TokenResponse>

}
