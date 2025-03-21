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

        val loginButton: Button = view.findViewById(R.id.saveButton)
        val emailTextField: TextView = view.findViewById(R.id.userEmailView)
        val passwordTextField: TextView = view.findViewById(R.id.userPasswordView)

        loginButton.setOnClickListener {
            val email = emailTextField.text.toString().trim()
            val password = passwordTextField.text.toString().trim()

            firebaseModel.login(email, password) { user ->
                if (user != null) {
                    currentUser = user
                    Log.d("LoginFragment", "Login successful: ${user.email}")
                    navigateToHome()
                } else {
                    // Try to register instead
                    firebaseModel.register(email, password) { newUser ->
                        if (newUser != null) {
                            Log.d("LoginFragment", "Registered new user: ${newUser.email}")
                            navigateToHome()
                        } else {
                            Log.d("LoginFragment", "Login and registration both failed.")
                        }
                    }
                }
            }
        }
    }

    private fun navigateToHome() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true) // clears back history
            .build()

        findNavController().navigate(R.id.action_loginFragment_to_homeFragment, null, navOptions)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()

    }
}