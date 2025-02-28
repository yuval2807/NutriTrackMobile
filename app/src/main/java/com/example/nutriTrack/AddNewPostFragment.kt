package com.example.nutriTrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nutriTrack.Model.Post
import com.google.android.material.textfield.TextInputEditText


class AddNewPostFragment : Fragment() {
    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var categoryDropdown: AutoCompleteTextView
    private lateinit var saveButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_new_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleEditText = view.findViewById(R.id.et_post_title)
        descriptionEditText = view.findViewById(R.id.et_post_description)
        categoryDropdown = view.findViewById(R.id.et_category)
        saveButton = view.findViewById(R.id.btn_save_post)

        // Set up category dropdown
        val categories = resources.getStringArray(R.array.categories_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        categoryDropdown.setAdapter(adapter)
        categoryDropdown.setOnClickListener { categoryDropdown.showDropDown() }


        saveButton.setOnClickListener {
            savePost()
        }
    }

    private fun savePost() {
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val category = categoryDropdown.text.toString().trim()
        val image = "image"


        if (title.isEmpty() || description.isEmpty() || category.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Simulate saving post (you can replace this with database logic)
        val newPost = Post("test", title, description, category, image )
        Post.addPost(newPost)
        // Display a success message
        Toast.makeText(requireContext(), "Post saved successfully!", Toast.LENGTH_SHORT).show()
    }
}

