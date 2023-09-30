package com.example.mygithubapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapp.adapter.FollowItemAdapter
import com.example.mygithubapp.data.response.FollowResponseItem
import com.example.mygithubapp.databinding.FragmentFollowBinding
import com.example.mygithubapp.ui.viewmodel.FollowViewModel


class FollowFragment : Fragment() {


    private lateinit var binding: FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        binding = FragmentFollowBinding.inflate(inflater, container, false)

        return binding.root
    }


    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }

    private var position: Int = 0
    private var username: String? = null

    private lateinit var followViewModel: FollowViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1){
            followViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            followViewModel.setUsername(username!!)

            followViewModel.following.observe(viewLifecycleOwner) { followDataList ->
                setFollowData(followDataList)
            }

        } else {
            followViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            followViewModel.setUsername(username!!)

            followViewModel.followers.observe(viewLifecycleOwner) { followDataList ->
                setFollowData(followDataList)
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)
    }

    private fun setFollowData(consumerReviews: List<FollowResponseItem>) {
        val adapter = FollowItemAdapter()
        adapter.submitList(consumerReviews)
        binding.recyclerView.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}


