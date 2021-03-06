package com.example.week_7_task.network

import com.example.week_7_task.*
import com.example.week_7_task.ui.pokelive.PokeliveViewModel
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UploadEndPoint {
    // Endpoint for posting images to the server
    @Multipart
    @POST("v1/upload")
    fun uploadImageAsync(@Part file : MultipartBody.Part): Deferred<UploadResponse>
}

