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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Model
import com.example.nutriTrack.Model.User
import com.example.nutriTrack.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val firebaseModel = FirebaseModel()
    private var postImageBitmap: Bitmap? = null
    private var postImageUri: Uri? = null

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                postImageBitmap = bitmap
                binding.imageView.setImageBitmap(bitmap)
            } ?: Toast.makeText(requireContext(), "Failed to capture photo", Toast.LENGTH_SHORT).show()
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                postImageUri = it
                binding.imageView.setImageURI(it)
            } ?: Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userIdInput.addTextChangedListener(object : TextWatcher {
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
                        //binding.registerButton.isEnabled = true
                    } else {
                        binding.userIdInput.error = "ID must be 9 digits long and contain only numbers"
                    }
                }
            }
        })

        binding.userPhoneInput.addTextChangedListener(object : TextWatcher {
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
                        //binding.registerButton.isEnabled = true
                    } else {
                        binding.userPhoneInput.error = "Phone number must be 10 digits long and contain only numbers"
                    }
                }
            }
        })

        binding.registerPictureBtn.setOnClickListener {
            cameraLauncher.launch(null)
        }

        binding.registerGalleryBtn.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.registerButton.setOnClickListener {
            binding.progressSpinner.visibility = View.VISIBLE

            val email = binding.userEmailInput.text.toString().trim()
            val password = binding.userPasswordInput.text.toString().trim()
            val name = binding.userNameInput.text.toString().trim()
            val id = binding.userIdInput.text.toString().trim()
            val phone = binding.userPhoneInput.text.toString().trim()

            register(email, password, name, id, phone)
        }
    }

    private fun register(email: String, password: String, name: String, id: String, phone: String) {
        firebaseModel.register(email, password) { user ->
            if (user != null) {
                val newUser = User(id, email, name, phone, postImageUri?.toString() ?: "")
                if (postImageBitmap != null) {
                    val bitmap = (binding.imageView.drawable as BitmapDrawable).bitmap

                    Model.shared.addUser(newUser, bitmap, Model.Storage.CLOUDINARY) {}
                } else {
                    User.addUser(newUser)
                }

                Log.d("registerFragment", "Registered new user: ${newUser.email}")
                binding.progressSpinner.visibility = View.GONE
                navigateToHome()

            } else {
                Log.d("registerFragment", "Login and registration both failed.")
                binding.progressSpinner.visibility = View.GONE
            }
        }
    }

    private fun navigateToHome() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true) // clears back stack
            .build()

        findNavController().navigate(R.id.action_registerFragment_to_homeFragment, null, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
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