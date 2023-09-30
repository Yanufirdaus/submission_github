package com.example.mygithubapp.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mygithubapp.database.Favorite
import com.example.mygithubapp.database.FavoriteDao
import com.example.mygithubapp.database.FavoriteRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository (application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<Favorite>> = mFavoriteDao.getAllFavorite()

    fun insert(favorite: Favorite) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }
    fun delete(favorite: Favorite) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }
    fun update(favorite: Favorite) {
        executorService.execute { mFavoriteDao.update(favorite) }
    }

    interface GetFavoriteByUsernameCallback {
        fun onFavoriteLoaded(favorite: Favorite?)
    }

    fun getFavoriteByUsername(username: String, callback: GetFavoriteByUsernameCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            val favorite = mFavoriteDao.getFavoriteByUsername(username)
            withContext(Dispatchers.Main) {
                callback.onFavoriteLoaded(favorite)
            }
        }
    }
}