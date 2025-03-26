package com.example.nutriTrack

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavOptions
import com.example.nutriTrack.Model.FirebaseModel
import com.google.firebase.auth.FirebaseUser
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    private lateinit var currentUser: FirebaseUser
    private lateinit var loginButton: Button
    private lateinit var emailInputField: EditText
    private lateinit var passwordInputField: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseModel = FirebaseModel()

        loginButton = view.findViewById(R.id.loginButton)
        val registerButton: Button = view.findViewById(R.id.nav_register_button)
        emailInputField = view.findViewById(R.id.userEmailView)
        passwordInputField = view.findViewById(R.id.userPasswordView)

        var email: String
        var password: String
        loginButton.isEnabled = false

        emailInputField.addTextChangedListener(createTextWatcher())
        passwordInputField.addTextChangedListener(createTextWatcher())

        loginButton.setOnClickListener {
            email = emailInputField.text.toString().trim()
            password = passwordInputField.text.toString().trim()

            firebaseModel.login(email, password) { user ->
                if (user != null) {
                    currentUser = user
                    navigateToHome()
                }
            }
        }

        registerButton.setOnClickListener {
            navigateToRegister()
        }
    }
    private fun createTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            // Check both fields aren't empty
            val email = emailInputField.text.toString().trim()
            val password = passwordInputField.text.toString().trim()

            loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty()
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

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()

    }
}