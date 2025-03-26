package com.example.nutriTrack

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Model
import com.example.nutriTrack.Model.Post
import com.example.nutriTrack.utils.getCurrDate
import com.google.android.material.textfield.TextInputEditText
import java.util.UUID

class AddNewPostFragment : Fragment() {
    public enum class PostMode {
        Edit,
        Add
    }

    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var dateTextView: TextView
    private lateinit var categoryDropdown: AutoCompleteTextView
    private lateinit var saveButton: Button
    private lateinit var takePictureButton: Button
    private lateinit var pickImageButton: Button
    private lateinit var postImageView: ImageView
    private lateinit var categories: Array<String>
    private var postImageBitmap: Bitmap? = null
    private var postImageUri: Uri? = null
    private var mode :PostMode = PostMode.Add
    private var postId: String? = null
    private var currPost: Post? = null

    private val cameraLauncher =
    registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            postImageBitmap = bitmap
            postImageView.setImageBitmap(bitmap)
        } ?: Toast.makeText(requireContext(), "Failed to capture photo", Toast.LENGTH_SHORT).show()
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                postImageUri = it
                postImageView.setImageURI(it)
            } ?: Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        val args: AddNewPostFragmentArgs by navArgs()
        postId = args.postId
        if (postId != null) {
                mode = PostMode.Edit
                loadPostData(postId!!)
        }

        return inflater.inflate(R.layout.fragment_add_new_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleEditText = view.findViewById(R.id.et_post_title)
        descriptionEditText = view.findViewById(R.id.et_post_description)
        dateTextView = view.findViewById(R.id.tv_date)
        categoryDropdown = view.findViewById(R.id.et_category)
        postImageView = view.findViewById(R.id.iv_post_image)
        saveButton = view.findViewById(R.id.btn_save_post)
        takePictureButton = view.findViewById(R.id.btn_take_picture)
        pickImageButton = view.findViewById(R.id.btn_pick_image)
        categories = resources.getStringArray(R.array.categories_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        categoryDropdown.setAdapter(adapter)
        categoryDropdown.setOnClickListener { categoryDropdown.showDropDown() }


        takePictureButton.setOnClickListener {
            cameraLauncher.launch(null)
        }

        pickImageButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        saveButton.setOnClickListener {
            savePost()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Custom back navigation logic
                findNavController().navigate(R.id.postsFragment)
            }
        })
    }

    private fun loadPostData(postId: String) {
        val firebaseModel = FirebaseModel()
        firebaseModel.getPostById(postId) { post ->
            if (post != null) {
                currPost = post

                 titleEditText.setText(post.getTitle())
                 descriptionEditText.setText(post.getDescription())
                categoryDropdown.setText(post.category.name, false)
                dateTextView.text = post.getDate()
                loadImageIntoImageView(postImageView,post.imageUrl)
            }
        }
    }

    private fun savePost() {
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val categoryText = categoryDropdown.text.toString().trim()

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

         var postId =  UUID.randomUUID().toString()

        if (mode == PostMode.Edit && currPost != null) {
            postId = currPost!!.id
        }


        val newPost = Post(
            postId,
            title,
            category,
            description,
            postImageUri?.toString() ?: "",
            "user1",
            getCurrDate()
        )

        if (postImageBitmap != null) {
            val bitmap = (postImageView.drawable as BitmapDrawable).bitmap

            Model.shared.add(newPost, bitmap, Model.Storage.CLOUDINARY) {
//                findNavController().navigate(R.id.action_addNewPost_to_postsFragment)
            }
        }
        Model.shared.add(newPost, null, Model.Storage.CLOUDINARY) {
}
        findNavController().navigate(R.id.action_addNewPost_to_postsFragment)


    }
}


