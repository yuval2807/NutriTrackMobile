package com.example.nutriTrack

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavOptions
import com.example.nutriTrack.Model.FirebaseModel
import com.google.firebase.auth.FirebaseUser
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    private lateinit var currentUser: FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseModel = FirebaseModel()

        val loginButton: Button = view.findViewById(R.id.loginButton)
        val registerButton: Button = view.findViewById(R.id.nav_register_button)
        val emailTextField: TextView = view.findViewById(R.id.userEmailView)
        val passwordTextField: TextView = view.findViewById(R.id.userPasswordView)

        var email: String
        var password: String

        loginButton.setOnClickListener {
            email = emailTextField.text.toString().trim()
            password = passwordTextField.text.toString().trim()

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