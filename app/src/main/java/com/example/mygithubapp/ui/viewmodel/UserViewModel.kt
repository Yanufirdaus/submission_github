package com.example.mygithubapp.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubapp.data.response.ItemsItem
import com.example.mygithubapp.data.response.UserResponse
import com.example.mygithubapp.data.retrofit.ApiConfig
import com.example.mygithubapp.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val _userdata = MutableLiveData<List<ItemsItem>>()
    val userdata: LiveData<List<ItemsItem>> = _userdata

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _rvUserVisibility = MutableLiveData<Int>()
    val rvUserVisibility: LiveData<Int> = _rvUserVisibility

    private val _notFoundLayoutVisibility = MutableLiveData<Int>()
    val notFoundLayoutVisibility: LiveData<Int> = _notFoundLayoutVisibility

    init {
        findUser()
    }

    fun findUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserdetail(MainActivity.USER_LOGIN)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.items.isEmpty()){// Notify UI that data is empty
                            _rvUserVisibility.value = View.GONE
                            _notFoundLayoutVisibility.value = View.VISIBLE
                        } else {
                            _userdata.value = responseBody.items
                            _rvUserVisibility.value = View.VISIBLE
                            _notFoundLayoutVisibility.value = View.GONE
                        }
                    }
                } else {
                    Log.e(MainActivity.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(MainActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }
}