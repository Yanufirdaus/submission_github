package com.example.mygithubapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubapp.data.response.ReposResponseItem
import com.example.mygithubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReposViewModel : ViewModel() {
    private val _reposdata = MutableLiveData<List<ReposResponseItem>>()
    val reposdata: LiveData<List<ReposResponseItem>> = _reposdata

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username


    fun setUsername(newUsername: String) {
        _username.value = newUsername
        findRepos()
    }

    private fun findRepos() {
        _isLoading.value = true
        val usernameValue = _username.value
        val client = ApiConfig.getApiService().getRepos(usernameValue)
        client.enqueue(object : Callback<List<ReposResponseItem>> {
            override fun onResponse(
                call: Call<List<ReposResponseItem>>,
                response: Response<List<ReposResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _reposdata.value = responseBody!!
                } else {
                    Log.e("TAG", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ReposResponseItem>>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.message}")
            }
        })
    }
}