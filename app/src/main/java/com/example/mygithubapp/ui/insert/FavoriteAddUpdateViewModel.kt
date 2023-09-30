package com.example.mygithubapp.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.mygithubapp.Repository.FavoriteRepository
import com.example.mygithubapp.database.Favorite

class FavoriteAddUpdateViewModel(application: Application) : ViewModel() {

    companion object {
        const val EXTRA_FAVORITE = "extra_favorite"
    }

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }
    fun update(favorite: Favorite) {
        mFavoriteRepository.update(favorite)
    }
    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }

    fun getFavoriteByUsername(username: String, callback: FavoriteRepository.GetFavoriteByUsernameCallback) {
        mFavoriteRepository.getFavoriteByUsername(username, callback)
    }
}