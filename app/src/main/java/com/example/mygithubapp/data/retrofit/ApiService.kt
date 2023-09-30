package com.example.mygithubapp.data.retrofit


import com.example.mygithubapp.data.response.FollowResponseItem
import com.example.mygithubapp.data.response.ReposResponseItem
import com.example.mygithubapp.data.response.UserDetailResponse
import com.example.mygithubapp.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUserdetail(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String?): Call<List<FollowResponseItem>>
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String?): Call<List<FollowResponseItem>>

    @GET("users/{username}/repos")
    fun getRepos(@Path("username") username: String?): Call<List<ReposResponseItem>>

}