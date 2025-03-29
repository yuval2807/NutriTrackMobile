package com.example.nutriTrack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Post
import com.example.nutriTrack.Model.User
import com.example.nutriTrack.ViewModel.PostsListViewModel
import com.example.nutriTrack.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var userEmail: String? = null
    private var user: User? = null
    private val firebaseModel = FirebaseModel()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var postList: MutableList<Post>
    private val viewModel: PostsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.progressSpinner.visibility = View.VISIBLE

        val recyclerView = binding.profilePostsListRv
        val swipeRefreshLayout = binding.profilePostsListRvSwipeRefresh

        val navController = findNavController()

        postList = mutableListOf()
        postListAdapter = PostListAdapter(postList, navController,true)

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

        userEmail = firebaseModel.getUserEmail()

        User.getUserByEmail(userEmail!!) { fullUser ->
            if (fullUser != null) {
                user = fullUser
                user?.let {

                    binding.userNameView.text = it.name
                    binding.userIdView.text = it.id
                    binding.userPhoneView.text = it.phone
                    loadImageIntoImageView(binding.imageView, it.imageUrl)
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

        // Adjust layout to avoid cut-off by status bar
        val rootView = view.findViewById<FrameLayout>(R.id.profile_main)
        rootView.setOnApplyWindowInsetsListener { v, insets ->
            val statusBarHeight = insets.systemGestureInsets.top
            v.setPadding(v.paddingLeft, statusBarHeight, v.paddingRight, v.paddingBottom)
            insets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        viewModel.getPosts(firebaseModel.getUserDocumentNumber()).observe(viewLifecycleOwner) { posts ->
            Log.d("ProfileFragment", " get posts viewModel ${posts}")
            postListAdapter.setData(posts)
            binding.progressSpinner.visibility = View.GONE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_post_details, menu)
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
