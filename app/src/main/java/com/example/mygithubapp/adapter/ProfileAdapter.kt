package com.example.mygithubapp.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubapp.data.response.ItemsItem
import com.example.mygithubapp.databinding.UserListItemBinding
import com.example.mygithubapp.ui.DetailUser

class ProfileAdapter : ListAdapter<ItemsItem, ProfileAdapter.MyViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

    }

    class MyViewHolder(val binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem){
            binding.userName.text = user.login

            Glide.with(binding.imageProfile.context)
                .load(user.avatarUrl)
                .into(binding.imageProfile)

            binding.buttonGithub.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/"+user.login))
                it.context.startActivity(intent)
            }
            binding.userCardView.setOnClickListener {
                val intent = Intent(binding.root.context, DetailUser::class.java)
                intent.putExtra("userName", user.login)
                binding.root.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.login == newItem.login
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}