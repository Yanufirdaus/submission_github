package com.example.mygithubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapp.adapter.ReposAdapter
import com.example.mygithubapp.data.response.ReposResponseItem
import com.example.mygithubapp.databinding.ActivityReposBinding
import com.example.mygithubapp.ui.viewmodel.ReposViewModel

class ReposActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReposBinding
    private lateinit var reposViewModel: ReposViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReposBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")

        binding.rvRepos.layoutManager = LinearLayoutManager(this)

        reposViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ReposViewModel::class.java)

        reposViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        reposViewModel.setUsername(username!!)

        reposViewModel.reposdata.observe(this) { reposDataList ->
            setReposData(reposDataList)
        }
    }

    private fun setReposData(ReposList: List<ReposResponseItem>) {
        val adapter = ReposAdapter()
        adapter.submitList(ReposList)
        binding.rvRepos.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}