package com.example.billbill_template.post


//게시물 목록
data class GetPostsResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: GetPostsData?
)
data class GetPostsData(
    val posts: List<GetPostsPosts>
)
data class GetPostsPosts(
    val id: Int,
    val author: GetPostsAuthor,
    val itemName : String,
    val description: String,
    val itemCondition: String,
    val thumbnail: String,
    val area: Int, //Int 맞는지 확인 필요
    val price: Int,
    val deposit: Int
)
data class GetPostsAuthor(
    val id: Int,
    val name: String,
    val avatar: String
)


//게시물 상세 조회
data class GetPostByIdResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: GetPostByIdData?
)
data class GetPostByIdData(
    val post: List<GetPostByIdPost>
)
data class GetPostByIdPost(
    val id: Int,
    val author: PostAuthor,
    val itemName: String,
    val itemCondition: String,
    val category: Int,
    val images: List<PostImages>?,
    val description: String,
    val price: Int,
    val deposit: Int,
    val area: Int,
    val dateBegin: Int,
    val dateEnd: Int,
    val isLent: Boolean
)
data class PostAuthor(
    val id: Int,
    val name: String,
    val avatar: String
)
data class PostImages(
    val id: Int,
    val url: String
)


//게시물 작성
data class CreatePostRequest(
    val itemName: String,
    val description: String,
    val categoryId: Int,
    val areaCode: Int,
    val price: Int,
    val deposit: Int,
    val itemCondition: String, //Enum으로 수정 필요
    val dateBegin: Int,
    val dateEnd: Int?
)
data class CreatePostResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: CreatePostId?
)
data class CreatePostId(
    val createdPostId: Int
)


//게시글 수정
data class EditPostRequest(
    val itemName: String?,
    val description: String?,
    val price: Int?,
    val deposit: Int?,
    val itemCondition: String?,
    val dateBegin: Int?,
    val dateEnd: Int?
)
data class EditPostResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: Any?
)


//게시글 삭제
data class DeletePostResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: Any?
)


//게시글 사진 업로드
//data class UploadPostImageRequest()
data class UploadPostImageResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: Any?
)


//게시글 사진 삭제
data class DeletePostImageResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: Any?
)


//게시글 찜하기
data class AddPostFavoriteResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: Any?
)