import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.net.URISyntaxException

object SocketManager {
    private lateinit var socket: Socket

    fun initializeSocket(chattingId: String, token: String) {
        try {

//            // 커스텀 OkHttpClient를 사용해 토큰을 HTTP 요청 헤더에 추가
//            val client = OkHttpClient.Builder()
//                .addInterceptor(object : Interceptor {
//                    @Throws(IOException::class)
//                    override fun intercept(chain: Interceptor.Chain): Response {
//                        val original = chain.request()
//                        val requestBuilder = original.newBuilder()
//                            .header("Authorization", "$token")
//                        val request = requestBuilder.build()
//                        return chain.proceed(request)
//                    }
//                }).build()

            val options = IO.Options().apply {

//                callFactory = client
//                webSocketFactory = client

                // Socket auth에 JWT 토큰 추가
                auth = mapOf("ACCESS_TOKEN" to token)

                // 필요시 커스텀 OkHttpClient 사용 가능
                callFactory = OkHttpClient()
            }
            // 서버에 연결
            socket = IO.socket("http://ec2-54-180-208-31.ap-northeast-2.compute.amazonaws.com:3000/", options)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun getSocket(): Socket = socket

    fun connect() {
        socket.connect()
    }

    fun disconnect() {
        socket.disconnect()
    }
}