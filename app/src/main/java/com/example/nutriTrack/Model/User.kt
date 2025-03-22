package com.example.nutriTrack.Model

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

        fun updateUser(userid:String, updateUser: User) {
        }

        fun getUserByEmail(email: String, callback: (User?) -> Unit) {
            firebaseModel.getUserInfoByEmail(email) { user ->
                callback(user)
            }
        }
    }
}