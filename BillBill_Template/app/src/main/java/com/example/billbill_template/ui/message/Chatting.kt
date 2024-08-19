package com.example.billbill_template.ui.message

//채팅방(쪽지) 목록 조회
data class GetChattingsResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: GetChattingsData?
)
data class GetChattingsData(
    val chattings: List<GetChatting>?
)
data class GetChatting(
    val id: Int,
    val user: GetChattingUser,
    val post: GetChattingPost,
    val lastMessage: GetChattingLastMessage?
)
data class GetChattingUser(
    val id: Int,
    val email: String,
    val avatar: String?
)
data class GetChattingPost(
    val id: Int,
    val itemName: String,
    val price: Int,
    val deposit: Int,
)
data class GetChattingLastMessage(
    val id: Int,
    val senderId: Int,
    val type: String,
    val content: String,
    val createAt: Int
)

//채팅방 생성
data class CreateChattingResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: CreateChattingData?
)
data class CreateChattingData(
    val chatId: Int
)

//채팅 상세조회
data class GetChattingByIdResponse(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: GetChattingByIdData?
)
data class GetChattingByIdData(
    val id: Int,
//    val to: , 백엔드 미구현
    val post: GetChattingByIdPost,
    val socketNamespace: String,
    val messages: List<GetChattingByIdMessages>
)
data class GetChattingByIdPost(
    val id: Int,
    val itemName: String,
    val price: Int,
    val deposit: Int,
//    val image: List<> 백엔드 미구현
)
data class GetChattingByIdMessages(
    val id: Int,
    val byMe: Boolean,
    val type: String,
    val message: String?,
    val image: GetChattingByIdMessagesImage?,
    val sendAt: Int
)
data class GetChattingByIdMessagesImage(
    val id: Int,
    val url: String
)

//채팅방 사진 업로드
data class GetChattingById2Response(
    val success: Boolean,
    val code: String?,
    val message: String?,
    val data: GetChattingById2Data?
)
data class GetChattingById2Data(
    val image: GetChattingById2Image
)
data class GetChattingById2Image(
    val id: Int,
    val url: String
)