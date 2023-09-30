package com.example.mygithubapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubapp.data.response.FollowResponseItem
import com.example.mygithubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _following = MutableLiveData<List<FollowResponseItem>>()
    val following: LiveData<List<FollowResponseItem>> = _following

    private val _followers = MutableLiveData<List<FollowResponseItem>>()
    val followers: LiveData<List<FollowResponseItem>> = _followers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username


    fun setUsername(newUsername: String) {
        _username.value = newUsername
        findFollowing()
        findFollowers()
    }


    private fun findFollowing() {
        _isLoading.value = true
        val usernameValue = _username.value
        val client = ApiConfig.getApiService().getFollowing(usernameValue)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _following.value = responseBody!!
                } else {
                    Log.e("TAG", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e("TAG", "onFailure: ${t.message}")
            }
        })
    }

    private fun findFollowers() {
        _isLoading.value = true
        val usernameValue = _username.value
        val client = ApiConfig.getApiService().getFollowers(usernameValue)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _followers.value = responseBody!!
                } else {
                    Log.e("TAG", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e("TAG", "onFailure: ${t.message}")
            }
        })
    }
}

