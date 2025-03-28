package com.example.nutriTrack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.User

class ProfileFragment : Fragment() {

    private var userEmail: String? = null
    private var user: User? = null
    private val firebaseModel = FirebaseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setHasOptionsMenu(true) // Enable menu for this fragment

        val imageView: ImageView = view.findViewById(R.id.imageView)

        userEmail = firebaseModel.getUserEmail()

        User.getUserByEmail(userEmail!!) { fullUser ->
            if (fullUser != null) {
                user = fullUser
                user?.let {
                    view.findViewById<TextView>(R.id.userNameView).text = it.name
                    view.findViewById<TextView>(R.id.userIdView).text = it.id
                    view.findViewById<TextView>(R.id.userPhoneView).text = it.phone
                    loadImageIntoImageView(imageView, it.imageUrl)
                }

            } else {
                Log.d("userInfo", "User not found")
            }
        }

        // Logout button click handler
        view.findViewById<Button>(R.id.logout_button).setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        firebaseModel.signOut()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true) // clears back stack
            .build()

        findNavController().navigate(R.id.action_profileFragment_to_splashFragment, null, navOptions)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_post_details, menu)
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_edit -> navigateToEditStudent()
//
//        }
//        return super.onOptionsItemSelected(item)
//    }
//    override fun onResume() {
//        super.onResume()
//        (activity as? MainActivity)?.setToolbarTitle("Student Details")
//    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
