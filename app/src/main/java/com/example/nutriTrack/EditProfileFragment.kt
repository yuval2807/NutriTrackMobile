package com.example.nutriTrack

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Model
import com.example.nutriTrack.Model.User
import com.example.nutriTrack.databinding.FragmentEditProfileBinding
import com.example.nutriTrack.loadImageIntoImageView

class EditProfileFragment: Fragment() {

//    private lateinit var nameTextField: EditText
    private lateinit var userId: String
//    private lateinit var phoneNumberTextField: EditText
    private lateinit var user: User
    private val firebaseModel = FirebaseModel()
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var postImageView: ImageView
    private var postImageBitmap: Bitmap? = null
    private var postImageUri: Uri? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postImageView = binding?.imageView!!

        val userEmail = arguments?.getString("user_email")

        User.getUserByEmail(userEmail!!) { fullUser ->
            if (fullUser != null) {
                user = fullUser
                user?.let {

                    binding.userNameView.setText(it.name)
                    userId = it.id
//                    binding.userIdView.setText(it.id)
                    binding.userPhoneView.setText(it.phone)
                    loadImageIntoImageView(postImageView!!, it.imageUrl)
                }

            } else {
                Log.d("userInfo", "User not found")
            }
        }

        binding.takePictureBtn.setOnClickListener {
            cameraLauncher.launch(null)
        }

        binding.galleryBtn.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        // Save button click handler
        binding.saveButton.setOnClickListener {
            val updatedUser = User(
                user.id,
                user.email,
                binding.userNameView.text.toString(),
                binding.userPhoneView.text.toString(),
                postImageUri?.toString() ?: user.imageUrl
            )

            if ( postImageBitmap  != null) {
                val bitmap = (postImageView.drawable as BitmapDrawable).bitmap

                Model.shared.addUser(updatedUser, bitmap, Model.Storage.CLOUDINARY) {
                    findNavController().navigate(
                        R.id.action_editProfileFragment_to_profileFragment, null
                    )
                }
            } else {
                User.updateUser(userId, updatedUser) {
                    findNavController().navigate(
                        R.id.action_editProfileFragment_to_profileFragment, null
                    )
                }
            }
        }

//        private fun register(email: String, password: String, name: String, id: String, phone: String) {
//            firebaseModel.register(email, password) { user ->
//                if (user != null) {
//                    val newUser = User(id, email, name, phone, postImageUri?.toString() ?: "")
//                    if ( postImageBitmap  != null) {
//                        val bitmap = (postImageView.drawable as BitmapDrawable).bitmap
//
//                        Model.shared.addUser(newUser, bitmap, Model.Storage.CLOUDINARY) {}
//                    } else {
//                        User.addUser(newUser)
//                    }
//
//                    Log.d("registerFragment", "Registered new user: ${newUser.email}")
//                    navigateToHome()
//
//                } else {
//                    Log.d("registerFragment", "Login and registration both failed.")
//                }
//            }
//        }

        // Cancel button click handler
        binding.cancelButton.setOnClickListener {
//            parentFragmentManager.popBackStack()
            findNavController().navigate(
                R.id.action_editProfileFragment_to_profileFragment, null
            )
        }
    }

//    override fun onResume() {
//        super.onResume()
//        (activity as? MainActivity)?.setToolbarTitle("Edit Student")
//    }

    private fun showSaveSuccessDialog(updatedStudent: User) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Save Successful")
            .setMessage("The student details have been saved successfully.")
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("OK") { _, _ ->
                    // Navigate to the profileFragment
                findNavController().navigate(
                    R.id.action_editProfileFragment_to_profileFragment, null
                )
            }
            .create()

        dialog.show()
    }

    companion object {
        fun newInstance(userEmail: String): EditProfileFragment {
            val fragment = EditProfileFragment()
            val args = Bundle()
            args.putString("user_email", userEmail)
            fragment.arguments = args
            return fragment
        }
    }
}
