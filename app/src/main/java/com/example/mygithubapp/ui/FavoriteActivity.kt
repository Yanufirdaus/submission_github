package com.example.mygithubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapp.adapter.FavoriteAdapter
import com.example.mygithubapp.database.Favorite
import com.example.mygithubapp.databinding.ActivityFavoriteBinding
import com.example.mygithubapp.ui.insert.FavoriteAddUpdateViewModel
import com.example.mygithubapp.ui.viewmodel.FavoriteViewModel
import com.example.mygithubapp.ui.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    private lateinit var favoriteAddUpdateViewModel: FavoriteAddUpdateViewModel
    private lateinit var mainViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mainViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteAddUpdateViewModel = obtainFavoriteAddUpdateViewModel(this@FavoriteActivity)
        mainViewModel.getAllFavorite().observe(this) { noteList ->
            if (noteList != null) {
                adapter.setListFavorite(noteList)
            }
        }

        adapter = FavoriteAdapter{ favorite ->
            favoriteAddUpdateViewModel.delete(favorite)
        }
        binding?.rvFav?.layoutManager = LinearLayoutManager(this)
        binding?.rvFav?.setHasFixedSize(true)
        binding?.rvFav?.adapter = adapter

        binding?.buttonBack?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun obtainFavoriteAddUpdateViewModel(activity: AppCompatActivity): FavoriteAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteAddUpdateViewModel::class.java)
    }
}