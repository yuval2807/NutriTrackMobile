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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.ViewModel.PostsListViewModel
import kotlinx.coroutines.launch

class PostListFragment : Fragment() {
    private lateinit var postListAdapter: PostListAdapter

    // Use by viewModels() delegate for proper ViewModel creation
    private val viewModel: PostsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.postsList_rv)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.postsListRv_swipeRefresh)
        val navController = findNavController()

        postListAdapter = PostListAdapter(emptyList(), navController)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postListAdapter

        // Observe posts from ViewModel
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            Log.d("PostListFragment", " get posts viewModel ${posts}")

            postListAdapter.setData(posts)
        }

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            Log.d("PostListFragment", " get posts re")

            getAllPosts()
            swipeRefreshLayout.isRefreshing = false
        }

        // Initial posts load
        getAllPosts()

        return view
    }

    private fun getAllPosts() {
        // Use lifecycleScope to launch coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                Log.d("PostListFragment", "before refreshAllPost")

                // Assuming your ViewModel has a method to fetch posts
                viewModel.refreshAllPost()
            } catch (e: Exception) {
                Log.e("PostListFragment", "Error fetching posts", e)
            }
        }
    }
}