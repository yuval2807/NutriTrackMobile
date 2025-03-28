package com.example.nutriTrack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Post
import com.example.nutriTrack.Model.User
import com.example.nutriTrack.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var userEmail: String? = null
    private var user: User? = null
    private val firebaseModel = FirebaseModel()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var postList: MutableList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setHasOptionsMenu(true) // Enable menu for this fragment

        val imageView: ImageView? = binding?.imageView

        userEmail = firebaseModel.getUserEmail()

        User.getUserByEmail(userEmail!!) { fullUser ->
            if (fullUser != null) {
                user = fullUser
                user?.let {

                    binding.userNameView.text = it.name
                    binding.userIdView.text = it.id
                    binding.userPhoneView.text = it.phone
//                    view.findViewById<TextView>(R.id.userIdView).text = it.id
//                    view.findViewById<TextView>(R.id.userPhoneView).text = it.phone
                    loadImageIntoImageView(imageView!!, it.imageUrl)
                }

            } else {
                Log.d("userInfo", "User not found")
            }
        }

        // Logout button click handler
        binding.logoutButton.setOnClickListener {
            logout()
        }

        // Edit button click handler
        binding.editButton.setOnClickListener {
            navigateEditProfile()
        }
    }

    private fun navigateEditProfile() {
        val bundle = Bundle().apply {
            putString("user_email", userEmail)
        }

        findNavController().navigate(
            R.id.action_profileFragment_to_editProfileFragment,
            bundle
        )
    }

    private fun logout() {
        firebaseModel.signOut()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true) // clears back stack
            .build()

        findNavController().navigate(R.id.action_profileFragment_to_splashFragment, null, navOptions)
    }

    private fun loadPosts() {
        postList.clear()
        val firebaseModel = FirebaseModel()
        firebaseModel.getAllPosts { posts ->
            postList = posts
            postListAdapter.setData(postList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_post_details, menu)
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_edit -> navigateToEditStudent()
//
//        }
//        return super.onOptionsItemSelected(item)
//    }
//    override fun onResume() {
//        super.onResume()
//        (activity as? MainActivity)?.setToolbarTitle("Student Details")
//    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
