package com.example.week_7_task.secondimplementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

class SendImageViewModel : ViewModel() {
    private val _uploadImageResponse = MutableLiveData<String>()
    val uploadImageResponse: LiveData<String>
        get() = _uploadImageResponse


    /**
     * Receives the file and Request body and uses it to create the MultipartBody.Part
     */
    fun uploadImage(file: File, requestFile: RequestBody) {

        val reqBody = MultipartBody.Part
                .createFormData("file", file.name, requestFile)

        viewModelScope.launch {
            val getRequestDeferred = NetworkClient.getUploadEndpointApi().uploadImageAsync(reqBody)
            try {
                val result = getRequestDeferred.await()
                _uploadImageResponse.value = result.message
            } catch (e: Exception) {
                _uploadImageResponse.value = "Failure: ${e.message}"
            }
        }
    }
}
