package com.example.nutriTrack

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Post
import com.example.nutriTrack.Model.Post.Category
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class AddNewPostFragment : Fragment() {
    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var categoryDropdown: AutoCompleteTextView
    private lateinit var saveButton: Button
    private lateinit var takePictureButton: Button
    private lateinit var pickImageButton: Button
    private lateinit var postImageView: ImageView
    private var postImageBitmap: Bitmap? = null
    private var postImageUri: Uri? = null

    // Camera Launcher
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                postImageBitmap = it
                postImageView.setImageBitmap(it)
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
        return inflater.inflate(R.layout.fragment_add_new_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleEditText = view.findViewById(R.id.et_post_title)
        descriptionEditText = view.findViewById(R.id.et_post_description)
        categoryDropdown = view.findViewById(R.id.et_category)
        saveButton = view.findViewById(R.id.btn_save_post)
        takePictureButton = view.findViewById(R.id.btn_take_picture)
        pickImageButton = view.findViewById(R.id.btn_pick_image)
        val categories = resources.getStringArray(R.array.categories_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        categoryDropdown.setAdapter(adapter)
        categoryDropdown.setOnClickListener { categoryDropdown.showDropDown() }

        // Picture
        takePictureButton.setOnClickListener {
            cameraLauncher.launch(null)
        }

        pickImageButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        saveButton.setOnClickListener {
            savePost()
        }
    }

    private fun savePost() {
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val categoryText = categoryDropdown.text.toString().trim()
        val imgId: String = title + "_" + Timestamp(System.currentTimeMillis())

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

        val postId = UUID.randomUUID().toString()
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatted = current.format(formatter)
        val newPost = Post(postId, title, category, description, postImageUri?.toString() ?: "","user1",formatted)

        val firebaseModel = FirebaseModel()
        firebaseModel.add(newPost)

        // Display a success message


        if (postImageBitmap != null) {
            val bitmap = (postImageView.drawable as BitmapDrawable).bitmap

            //Model is changing to firebase model
//            Model.instance().uploadImage(imgId, bitmap) { imageUrl ->
//                if (imageUrl != null) {
//                    post.setImageUrl(imageUrl)
//                }
//                Model.instance().addPost(post)
//
//            }


        }
                // Add post to list
//        Post.addPost(post)


        // Simulate saving the post
        Toast.makeText(requireContext(), "Post saved successfully!", Toast.LENGTH_SHORT).show()
    }

//
}

