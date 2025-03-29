package com.example.nutriTrack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutriTrack.ViewModel.PostsListViewModel
import com.example.nutriTrack.databinding.FragmentPostsBinding
import kotlinx.coroutines.launch

class PostListFragment : Fragment() {
    private lateinit var postListAdapter: PostListAdapter
    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    // Use by viewModels() delegate for proper ViewModel creation
    private val viewModel: PostsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        postListAdapter = PostListAdapter(emptyList(), navController, false)

        binding.postsListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.postsListRv.adapter = postListAdapter

        // Observe posts from ViewModel
        viewModel.getPosts(null).observe(viewLifecycleOwner) { posts ->
            Log.d("PostListFragment", " get posts viewModel ${posts}")
            postListAdapter.setData(posts)
        }

        // Set up SwipeRefreshLayout
        binding.postsListRvSwipeRefresh.setOnRefreshListener {
            Log.d("PostListFragment", " get posts re")

            getAllPosts()
            binding.postsListRvSwipeRefresh.isRefreshing = false
        }

        getAllPosts()
    }

    private fun getAllPosts() {
        viewModel.refreshAllPost()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                viewModel.refreshAllPost()
            } catch (e: Exception) {
                Log.e("getAllPosts", "Error fetching posts", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}