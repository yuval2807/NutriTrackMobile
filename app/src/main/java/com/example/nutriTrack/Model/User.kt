package com.example.nutriTrack.Model

import android.util.Log

data class User(
    var id: String = "",
    var email: String = "",
    var name: String = "",
    var phone: String = "",
    var imageUrl: String= ""
) {
    companion object {

        const val COLLECTION_NAME = "users"
        private val firebaseModel = FirebaseModel()

        fun addUser(user: User) {
            firebaseModel.addUser(user)
        }

        fun updateUser(userId:String, updatedUser: User, callback: (User?) -> Unit) {
            Log.d("updateUser", "user: $updatedUser")
            firebaseModel.updateUser(updatedUser, userId) { user ->
               callback(user)
            }
        }

        fun getUserByEmail(email: String, callback: (User?) -> Unit) {
            firebaseModel.getUserInfoByEmail(email) { user ->
                callback(user)
            }
        }
    }
}