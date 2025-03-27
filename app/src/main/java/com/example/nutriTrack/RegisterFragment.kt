package com.example.nutriTrack

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Model
import com.example.nutriTrack.Model.User

class RegisterFragment : Fragment() {

    private val firebaseModel = FirebaseModel()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerButton: Button = view.findViewById(R.id.registerButton)
        val takePictureButton: Button = view.findViewById(R.id.register_picture_btn)
        val pickImageButton: Button = view.findViewById(R.id.register_gallery_btn)

        postImageView = view.findViewById(R.id.imageView)
        val nameTextField: EditText = view.findViewById(R.id.userNameInput)
        val idTextField: EditText = view.findViewById(R.id.userIdInput)
        val emailTextField: EditText = view.findViewById(R.id.userEmailInput)
        val passwordTextField: EditText = view.findViewById(R.id.userPasswordInput)
        val phoneTextField: EditText = view.findViewById(R.id.userPhoneInput)

//        registerButton.isEnabled = false

        idTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }

            override fun afterTextChanged(editable: Editable?) {
                val id = editable.toString()

                if (id.isNotEmpty()) {
                    if (isValidId(id)) {
                        //registerButton.isEnabled = true
                    } else {
                        idTextField.error = "ID must be 9 digits long and contain only numbers"
                    }
                }
            }
        })

        phoneTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }

            override fun afterTextChanged(editable: Editable?) {
                val phoneNumber = editable.toString()

                if (phoneNumber.isNotEmpty()) {
                    if (isValidPhoneNumber(phoneNumber)) {
                        //registerButton.isEnabled = true
                    } else {
                        phoneTextField.error = "Phone number must be 10 digits long and contain only numbers"
                    }
                }
            }
        })

        takePictureButton.setOnClickListener {
            cameraLauncher.launch(null)
        }

        pickImageButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        registerButton.setOnClickListener {
            val email = emailTextField.text.toString().trim()
            val password = passwordTextField.text.toString().trim()
            val name = nameTextField.text.toString().trim()
            val id = idTextField.text.toString().trim()
            val phone = phoneTextField.text.toString().trim()

            register(email, password, name, id, phone)
        }
    }

    private fun register(email: String, password: String, name: String, id: String, phone: String) {
        firebaseModel.register(email, password) { user ->
            if (user != null) {
                val newUser = User(id, email, name, phone, postImageUri?.toString() ?: "")
                if ( postImageBitmap  != null) {
                    val bitmap = (postImageView.drawable as BitmapDrawable).bitmap

                    Model.shared.addUser(newUser, bitmap, Model.Storage.CLOUDINARY) {}
                } else {
                    User.addUser(newUser)
                }

                Log.d("registerFragment", "Registered new user: ${newUser.email}")
                navigateToHome()

            } else {
                Log.d("registerFragment", "Login and registration both failed.")
            }
        }
    }

    private fun navigateToHome() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true) // clears back stack
            .build()

        findNavController().navigate(R.id.action_registerFragment_to_homeFragment, null, navOptions)
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        // Regex for 10 numbers
        val regex = "^[0-9]{10}$"
        return phone.matches(regex.toRegex())
    }

    private fun isValidId(id: String): Boolean {
        // Regex for 9 numbers
        val regex = "^[0-9]{9}$"
        return id.matches(regex.toRegex())
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegisterFragment()
    }
}