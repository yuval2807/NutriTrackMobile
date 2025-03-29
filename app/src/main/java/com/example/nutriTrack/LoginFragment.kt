package com.example.nutriTrack

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import com.example.nutriTrack.Model.FirebaseModel
import com.google.firebase.auth.FirebaseUser
import androidx.navigation.fragment.findNavController
import com.example.nutriTrack.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseModel = FirebaseModel()

        binding.loginButton.isEnabled = false

        binding.userEmailView.addTextChangedListener(createTextWatcher())
        binding.userPasswordView.addTextChangedListener(createTextWatcher())

        binding.loginButton.setOnClickListener {
            binding.progressSpinner.visibility = View.VISIBLE

            val email = binding.userEmailView.text.toString().trim()
            val password = binding.userPasswordView.text.toString().trim()

            firebaseModel.login(email, password) { user ->
                if (user != null) {
                    currentUser = user
                    binding.progressSpinner.visibility = View.GONE
                    navigateToHome()
                }
            }
        }

        binding.navRegisterButton.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun createTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            // Check both fields aren't empty
            val email = binding.userEmailView.text.toString().trim()
            val password = binding.userPasswordView.text.toString().trim()

            binding.loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty()
        }
    }

    private fun navigateToHome() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true) // clears back stack
            .build()

        findNavController().navigate(R.id.action_loginFragment_to_homeFragment, null, navOptions)
    }

    private fun navigateToRegister() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}