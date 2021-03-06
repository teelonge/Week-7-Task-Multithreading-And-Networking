import com.example.week_7_task.network.UploadEndPoint

import com.example.week_7_task.BASE_UPLOAD_URL
import com.example.week_7_task.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit

object NetworkClient {

    fun getUploadEndpointApi(): UploadEndPoint {

        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(logging).build())
            .baseUrl(BASE_UPLOAD_URL)
            .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(UploadEndPoint::class.java)
    }


}