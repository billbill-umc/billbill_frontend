package com.example.billbill_template.post

import com.example.billbill_template.Login.signup.RetrofitClient
import okhttp3.Callback
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostService {
    @GET("/posts")
    fun getPosts(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("area") area: List<String>,
        @Query("category") category: Int?
    ) : Call<GetPostsResponse>


    @GET("/posts/{postId}")
    fun getPostById(@Path("postId") postId : Int) : Call<GetPostByIdResponse>


    @POST("/posts")
    fun createPost(@Body createPostRequest: CreatePostRequest) : Call<CreatePostResponse>




    @PATCH("/posts/{postId}")
    fun editPost(@Path("postId") postId : Int,
                 @Body editPostRequest: EditPostRequest) : Call<EditPostResponse>


    @DELETE("/posts/{postId}")
    fun deletePost(@Path("postId") postId : Int) : Call<DeletePostResponse>


    @POST("/posts/{postId}/images")
    fun uploadPostImage(@Header("Content-Type") contentType: String,
                        @Path("postId") postId : Int) : Call<UploadPostImageResponse>


    @DELETE("/posts/{postId}/images/{imageId}")
    fun deletePostImage(
        @Path("postId") postId : Int,
        @Path("imageId") imageId : Int
    ) : Call<DeletePostImageResponse>



//    @POST("/posts/{postId}/favorite")
//    fun addPostFavorite(@Path("postId") postId : Int) : Call<AddPostFavoriteResponse>



    @GET("/manifest/area")
    fun getAreaManifest() : Call<GetAreaManifestResponse>


    @GET("/manifest/category")
    fun getCategoryManifest() : Call<GetCategoryManifestResponse>

}