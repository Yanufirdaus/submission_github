package com.example.mygithubapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubapp.database.Favorite
import com.example.mygithubapp.databinding.ItemFavoriteBinding
import com.example.mygithubapp.helper.FavoriteDiffCallback
import com.example.mygithubapp.ui.DetailUser

class FavoriteAdapter (private val onDeleteClickListener: (Favorite) -> Unit)  : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorite = ArrayList<Favorite>()
    @SuppressLint("NotifyDataSetChanged")
    fun setListFavorite(listFavorite: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorite, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }
    override fun getItemCount(): Int {
        return listFavorite.size
    }
    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            with(binding) {
                textView11.text = favorite.username
                button.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(favorite.github_url))
                    it.context.startActivity(intent)
                }
                Glide.with(imageView2.context)
                    .load(favorite.avatar_url)
                    .into(binding.imageView2)
                cardFav.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailUser::class.java)
                    intent.putExtra("userName", favorite.username)
                    binding.root.context.startActivity(intent)
                }
                button3.setOnClickListener {
                    onDeleteClickListener(favorite)
                }
            }
        }
    }
}