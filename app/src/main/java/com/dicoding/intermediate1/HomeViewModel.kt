package com.dicoding.intermediate1

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HomeViewModel(val context: Context): ViewModel() {

    var storyLoadState = MutableLiveData(false)
    val dataList = MutableLiveData<List<UserDataResponse>>()
    var loader = MutableLiveData(View.GONE)
    val error = MutableLiveData("")
    private val tag = HomeViewModel::class.simpleName

    fun loadStory(token: String) {
        loader.postValue(View.VISIBLE)
        val client = ApiConfig.getApiService().getAllStory(token, 30)
        client.enqueue(object : Callback<AllStoryResponse> {
            override fun onResponse(call: Call<AllStoryResponse>, response: Response<AllStoryResponse>) {
                if (response.isSuccessful) {
                    dataList.postValue(response.body()?.listStory)
                } else {
                    error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
                loader.postValue(View.GONE)
            }

            override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                loader.postValue(View.GONE)
                Log.e(tag, "onFailure Call: ${t.message}")
                error.postValue("${context.getString(R.string.error_load)} : ${t.message}")
            }
        })
    }

    fun newStory(token: String, image: File, description: String) {
        loader.postValue(View.VISIBLE)
        "${image.length() / 1080 / 1080} MB"
        val userDescription = description.toRequestBody("text/plain".toMediaType())
        val imageRequest = image.asRequestBody("image/jpg".toMediaTypeOrNull())
        val multiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            image.name,
            imageRequest
        )
        val clientResponse = ApiConfig.getApiService().startUploadImage(token, multiPart, userDescription)
        clientResponse.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                when (response.code()) {
                    401 -> error.postValue(context.getString(R.string.error_token_401))
                    413 -> error.postValue(context.getString(R.string.error_size_413))
                    201 -> storyLoadState.postValue(true)
                    else -> error.postValue("Error ${response.code()} : ${response.message()}")
                }

                loader.postValue(View.GONE)

            }

            override fun onFailure(call: Call<AddStoryResponse>, T: Throwable) {
                loader.postValue(View.GONE)
                Log.e(tag, "onFailure Call: ${T.message}")
                error.postValue("${context.getString(R.string.error_was_encountered)} : ${T.message}")
            }
        })
    }
}
