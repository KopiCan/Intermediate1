package com.dicoding.intermediate1

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationViewModel (val context: Context): ViewModel() {

    val loginState = MutableLiveData<LoginResponse>()
    val registerState = MutableLiveData<RegisterResponse>()

    var loader = MutableLiveData(View.GONE)
    val error = MutableLiveData("")
    val temporaryEmail = MutableLiveData("")
    private val tag = AuthenticationViewModel::class.simpleName

    fun login(email: String, password: String) {
        temporaryEmail.postValue(email)
        loader.postValue(View.VISIBLE)
        val clientResponse = ApiConfig.getApiService().startLogin(email, password)
        clientResponse.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>)
            {
                when(response.code()) {
                    400 -> error.postValue(context.getString(R.string.error_400))
                    401 -> error.postValue(context.getString(R.string.error_401))
                    200 -> loginState.postValue(response.body())
                    else -> error.postValue("ERROR ${response.code()}: ${response.message()}")
                }
                loader.postValue(View.GONE)
            }

            override fun onFailure(call: Call<LoginResponse>, T: Throwable) {
                loader.postValue(View.GONE)
                Log.e(tag, "onFailure Call : ${T.message}")
                error.postValue(T.message)
            }
        })
    }

    fun register(name: String, email: String, password: String){
        loader.postValue(View.VISIBLE)
        val clientResponse = ApiConfig.getApiService().startRegister(name, email, password)
        clientResponse.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                when (response.code()) {
                    400 -> error.postValue(context.getString(R.string.error_400))
                    201 -> registerState.postValue(response.body())
                    else -> error.postValue("ERROR ${response.code()} : ${response.errorBody()}")
                }
                loader.postValue((View.GONE))
            }

            override fun onFailure(call: Call<RegisterResponse>, T: Throwable) {
                loader.postValue(View.GONE)
                Log.e(tag, "onFailureCall: ${T.message}")
                error.postValue(T.message)
            }
        })
    }
}
