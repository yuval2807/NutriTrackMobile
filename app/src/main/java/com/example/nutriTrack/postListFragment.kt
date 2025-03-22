package com.example.nutriTrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Post

class postListFragment : Fragment() {

    private lateinit var postListAdapter: PostListAdapter
    private lateinit var postList: MutableList<Post>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.postsList_rv)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.postsListRv_swipeRefresh)

        val navController = findNavController()

        postList = mutableListOf()
        postListAdapter = PostListAdapter(postList, navController)

        postListAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postListAdapter

        swipeRefreshLayout.setOnRefreshListener {
            loadPosts()
            swipeRefreshLayout.isRefreshing = false
        }


        loadPosts()

        return view
    }

    private fun loadPosts() {
        postList.clear()
        val firebaseModel = FirebaseModel()
        firebaseModel.getAllPosts { posts ->
            postList = posts
            postListAdapter.setData(postList)
        }

    }
}
