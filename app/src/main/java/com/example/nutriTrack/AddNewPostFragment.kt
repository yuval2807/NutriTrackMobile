package com.example.nutriTrack

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Model
import com.example.nutriTrack.Model.Post
import com.example.nutriTrack.Model.User
import com.example.nutriTrack.databinding.FragmentAddNewPostBinding
import com.example.nutriTrack.utils.getCurrDate
import java.util.UUID

class AddNewPostFragment : Fragment() {
    public enum class PostMode {
        Edit,
        Add
    }

    private val firebaseModel = FirebaseModel()
    private var _binding: FragmentAddNewPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var categories: Array<String>
    private var postImageBitmap: Bitmap? = null
    private var postImageUri: Uri? = null
    private var mode: PostMode = PostMode.Add
    private var postId: String? = null
    private var currPost: Post? = null

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                postImageBitmap = bitmap
                binding.ivPostImage.setImageBitmap(bitmap)
            } ?: Toast.makeText(requireContext(), "Failed to capture photo", Toast.LENGTH_SHORT).show()
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                postImageUri = it
                binding.ivPostImage.setImageURI(it)
            } ?: Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddNewPostBinding.inflate(inflater, container, false)

        val args: AddNewPostFragmentArgs by navArgs()
        postId = args.postId
        if (postId != null) {
            mode = PostMode.Edit
            loadPostData(postId!!)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categories = resources.getStringArray(R.array.categories_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        binding.etCategory.setAdapter(adapter)
        binding.etCategory.setOnClickListener { binding.etCategory.showDropDown() }
        binding.tvDate.text = getCurrDate()

        val userEmail = firebaseModel.getUserEmail()

        User.getUserByEmail(userEmail!!) { fullUser ->
            if (fullUser != null) {
                fullUser.let {
                    binding.tvUsername.text = it.name
                    loadImageIntoImageView(binding.tvUserImage, it.imageUrl, R.drawable.image_placeholder)
                }
            } else {
                Log.d("userInfo", "User not found")
            }
        }

        binding.btnTakePicture.setOnClickListener {
            cameraLauncher.launch(null)
        }

        binding.btnPickImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnSavePost.setOnClickListener {
            savePost()
        }

        // Adjust layout to avoid cut-off by status bar
        val rootView = view.findViewById<FrameLayout>(R.id.add_post_root_layout)
        rootView.setOnApplyWindowInsetsListener { v, insets ->
            val statusBarHeight = insets.systemGestureInsets.top
            v.setPadding(v.paddingLeft, statusBarHeight, v.paddingRight, v.paddingBottom)
            insets
        }

    }

    private fun loadPostData(postId: String) {
        val firebaseModel = FirebaseModel()
        firebaseModel.getPostById(postId) { post ->
            if (post != null) {
                currPost = post

                binding.etPostTitle.setText(post.getTitle())
                binding.etPostDescription.setText(post.getDescription())
                binding.etCategory.setText(post.category.name, false)
                binding.tvDate.text = post.getDate()
                loadImageIntoImageView(binding.ivPostImage, post.imageUrl, R.drawable.image_placeholder)
            }
        }
    }

    private fun savePost() {
        binding.progressSpinner.visibility = View.VISIBLE

        val firebaseModel = FirebaseModel()
        val userDocNum = firebaseModel.getUserDocumentNumber()

        val title = binding.etPostTitle.text.toString().trim()
        val description = binding.etPostDescription.text.toString().trim()
        val categoryText = binding.etCategory.text.toString().trim()

        val category = try {
            Post.Category.valueOf(categoryText)
        } catch (e: IllegalArgumentException) {
            Toast.makeText(requireContext(), "Invalid category", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.isEmpty() || description.isEmpty() || category.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        var postId = UUID.randomUUID().toString()

        if (mode == PostMode.Edit && currPost != null) {
            postId = currPost!!.id
        }


        val newPost = Post(
            postId,
            title,
            category,
            description,
            postImageUri?.toString() ?: "",
            userDocNum,
            getCurrDate(),
            System.currentTimeMillis()
        )

        if (postImageBitmap != null) {
            val bitmap = (binding.ivPostImage.drawable as BitmapDrawable).bitmap
            Model.shared.addPost(newPost, bitmap, Model.Storage.CLOUDINARY) {}
        } else {
            Model.shared.addPost(newPost, null, Model.Storage.CLOUDINARY) {}
        }

        binding.progressSpinner.visibility = View.GONE

        findNavController().navigate(R.id.action_addNewPost_to_homeFragment, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


