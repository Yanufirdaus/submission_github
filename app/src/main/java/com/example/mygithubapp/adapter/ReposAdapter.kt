package com.example.mygithubapp.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mygithubapp.data.response.ReposResponseItem
import com.example.mygithubapp.databinding.ReposListItemBinding

class ReposAdapter : ListAdapter<ReposResponseItem, ReposAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ReposListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }
    class MyViewHolder(val binding: ReposListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repos: ReposResponseItem){
            binding.namaRepo.text = repos.name
            binding.ideRepo.text = repos.id.toString()
            binding.buttonLinkRepo.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/"+repos.owner.login+"/"+repos.name))
                it.context.startActivity(intent)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReposResponseItem>() {
            override fun areItemsTheSame(oldItem: ReposResponseItem, newItem: ReposResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ReposResponseItem, newItem: ReposResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}