package com.example.mygithubapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubapp.R
import com.example.mygithubapp.Repository.FavoriteRepository
import com.example.mygithubapp.adapter.FollowAdapter
import com.example.mygithubapp.data.response.UserDetailResponse
import com.example.mygithubapp.data.retrofit.ApiConfig
import com.example.mygithubapp.database.Favorite
import com.example.mygithubapp.databinding.ActivityDetailUserBinding
import com.example.mygithubapp.ui.insert.FavoriteAddUpdateViewModel
import com.example.mygithubapp.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUser : AppCompatActivity() {

//    private lateinit var binding: ActivityDetailUserBinding

//    private lateinit var activityDetailUserBinding: ActivityDetailUserBinding


    private var _activityDetailUserBinding: ActivityDetailUserBinding? = null
    private val binding get() = _activityDetailUserBinding

    companion object {
        private const val TAG = "DetailActivity"
        private var USER_NAME = ""
    }

    private var isEdit = false
    private var favorite: Favorite? = null
    private var avatarUrl = ""

    private lateinit var favoriteAddUpdateViewModel: FavoriteAddUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDetailUserBinding = ActivityDetailUserBinding.inflate(layoutInflater)
//        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        supportActionBar?.hide()

        binding?.buttonBack?.setOnClickListener {
            onBackPressed()
        }

        val userName = intent.getStringExtra("userName")
        if (userName != null) {
            USER_NAME = userName
        }
        detailUserGet()

        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val adapter = FollowAdapter(this)

        viewPager2.adapter = adapter
        adapter.username = USER_NAME

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Following"
                1 -> "Followers"
                else -> "Tab Default"
            }
        }.attach()

        tabLayout?.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.grey))

        binding?.reposButton?.setOnClickListener{
            val intent = Intent(this, ReposActivity::class.java)
            intent.putExtra("username", USER_NAME)
            startActivity(intent)
        }

        favoriteAddUpdateViewModel = obtainViewModel(this@DetailUser)

        favorite = intent.getParcelableExtra(FavoriteAddUpdateViewModel.EXTRA_FAVORITE)
        if (favorite != null) {
            isEdit = true
        } else {
            favorite = Favorite()
        }

        favoriteAddUpdateViewModel.getFavoriteByUsername(USER_NAME, object :
            FavoriteRepository.GetFavoriteByUsernameCallback{
            override fun onFavoriteLoaded(favorite: Favorite?) {
                if (favorite != null) {
                    binding?.fabAdd?.setImageResource(R.drawable.ic_fav_24)
                } else {
                    binding?.fabAdd?.setImageResource(R.drawable.ic_favborder)
                }
            }
        })

        binding?.fabAdd?.setOnClickListener {
            favorite?.username = USER_NAME
            favorite?.github_url = "https://github.com/$USER_NAME"
            favorite?.avatar_url = avatarUrl

            if (isEdit) {
                favoriteAddUpdateViewModel.update(favorite!!)
                showToast("Edited")
            } else {
                favoriteAddUpdateViewModel.getFavoriteByUsername(USER_NAME, object :
                    FavoriteRepository.GetFavoriteByUsernameCallback{
                    override fun onFavoriteLoaded(favorites: Favorite?) {
                        if (favorites != null) {
                            favoriteAddUpdateViewModel.delete(favorites)
                            showToast("Removed from Favorite User")
                            binding!!.fabAdd.setImageResource(R.drawable.ic_favborder)
                        } else {
                            favoriteAddUpdateViewModel.insert(favorite!!)
                            showToast("Added as Favorite User")
                            binding!!.fabAdd.setImageResource(R.drawable.ic_fav_24)
                        }
                    }
                })
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun detailUserGet() {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(USER_NAME)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    binding?.userNameDetail?.text  = response.body()?.login
                    binding?.userNameIdentity?.text  = response.body()?.name.toString().toUpperCase()
                    binding?.imageDetail?.let {
                        Glide.with(it.context)
                            .load(response.body()?.avatarUrl)
                            .into(binding!!.imageDetail)
                    }
                    binding?.tabs?.getTabAt(0)?.text = "following " + response.body()?.following
                    binding?.tabs?.getTabAt(1)?.text = "followers " + response.body()?.followers
                    binding?.reposButton?.text = "Repositories " + response.body()?.publicRepos
                    avatarUrl = response.body()?.avatarUrl.toString()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteAddUpdateViewModel::class.java)
    }
}