package com.example.billbill_template.Login.signup

import com.example.billbill_template.post.AddPostFavoriteResponse
import com.example.billbill_template.post.CreatePostRequest
import com.example.billbill_template.post.CreatePostResponse
import com.example.billbill_template.post.DeletePostImageResponse
import com.example.billbill_template.post.DeletePostResponse
import com.example.billbill_template.post.EditPostRequest
import com.example.billbill_template.post.EditPostResponse
import com.example.billbill_template.post.GetAreaManifestResponse
import com.example.billbill_template.post.GetCategoryManifestResponse
import com.example.billbill_template.post.GetPostByIdResponse
import com.example.billbill_template.post.GetPostsResponse
import com.example.billbill_template.post.UploadPostImageResponse
import com.example.billbill_template.ui.message.CreateChattingRequest
import com.example.billbill_template.ui.message.CreateChattingResponse
import com.example.billbill_template.ui.message.GetChattingById2Response
import com.example.billbill_template.ui.message.GetChattingByIdResponse
import com.example.billbill_template.ui.message.GetChattingsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
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

    //Post
    @GET("/posts")
    fun getPosts() : Call<GetPostsResponse>

    @GET("/posts")
    fun getPostsByCategory(@Query("category") category: Int?) : Call<GetPostsResponse>

    @GET("/posts/{postId}")
    fun getPostById(@Path("postId") postId : Int) : Call<GetPostByIdResponse>

    @POST("/posts")
    fun createPost(@Body createPostRequest: CreatePostRequest) : Call<CreatePostResponse>

    @PATCH("/posts/{postId}")
    fun editPost(@Path("postId") postId : Int,
                 @Body editPostRequest: EditPostRequest
    ) : Call<EditPostResponse>

    @DELETE("/posts/{postId}")
    fun deletePost(@Path("postId") postId : Int) : Call<DeletePostResponse>

    //Image
    @POST("/posts/{postId}/images")
    fun uploadPostImage(@Header("Content-Type") contentType: String,
                        @Path("postId") postId : Int) : Call<UploadPostImageResponse>

    @DELETE("/posts/{postId}/images/{imageId}")
    fun deletePostImage(@Path("postId") postId : Int,
                        @Path("imageId") imageId : Int) : Call<DeletePostImageResponse>

    //Favorite
    @POST("/posts/{postId}/favorite")
    fun addPostFavorite(@Path("postId") postId : Int) : Call<AddPostFavoriteResponse>

    //Chatting
    @GET("chattings")
    fun getChattings() : Call<GetChattingsResponse>

    @POST("chattings")
    fun createChatting(@Body createChattingRequest: CreateChattingRequest) : Call<CreateChattingResponse>

    @GET("/chattings/{chattingId}")
    fun getChattingById(@Path("chattingId") chattingId : Int) : Call<GetChattingByIdResponse>

    @POST("/chattings/{chattingId}/images")
    fun uploadChattingImage(@Header("Content-Type") contentType: String,
                            @Path("chattingId") chattingId : Int) : Call<GetChattingById2Response>

    //Manifest 조회
    @GET("/manifest/area")
    fun getAreaManifest() : Call<GetAreaManifestResponse>

    @GET("/manifest/category")
    fun getCategoryManifest() : Call<GetCategoryManifestResponse>
}
