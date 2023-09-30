package com.example.mygithubapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapp.R
import com.example.mygithubapp.adapter.ProfileAdapter
import com.example.mygithubapp.data.response.ItemsItem
import com.example.mygithubapp.database.FavoriteRoomDatabase
import com.example.mygithubapp.databinding.ActivityMainBinding
import com.example.mygithubapp.ui.viewmodel.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    companion object {
        const val TAG = "MainActivity"
        var USER_LOGIN = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = obtainViewModel(this@MainActivity)
        mainViewModel.getAllFavorite().observe(this) { noteList ->
            if (noteList != null) {

            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        binding.buttonBack.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }

        binding.rvUser.addItemDecoration(itemDecoration)

        val SearchView = findViewById<SearchView>(R.id.searchView)

        SearchView.queryHint = "Cari..."

        findUser()

        SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    p0?.let {
                        USER_LOGIN = it
                        findUser()

                    }
                    return true
                }
            }
        )
    }

    private fun findUser() {
        val userViewModel = ViewModelProvider(this@MainActivity, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        userViewModel.findUser()

        userViewModel.rvUserVisibility.observe(this@MainActivity) { rvUserVisibility ->
            binding.rvUser.visibility = rvUserVisibility
        }

        userViewModel.notFoundLayoutVisibility.observe(this@MainActivity) { notFoundLayoutVisibility ->
            binding.notFoundLayout.visibility = notFoundLayoutVisibility
        }

        userViewModel.isLoading.observe(this@MainActivity) { isLoading ->
            showLoading(isLoading)
        }

        userViewModel.userdata.observe(this@MainActivity) { userDataList ->
            setUserData(userDataList)
        }
    }

    private fun setUserData(UserData: List<ItemsItem>) {
        val adapter = ProfileAdapter()
        adapter.submitList(UserData)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
}