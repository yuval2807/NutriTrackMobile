package com.example.nutriTrack.Model

import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel {

    private val database = Firebase.firestore

    init {
        val setting = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }

        database.firestoreSettings = setting
    }

    fun getAllPosts(): MutableList<Post> {
        var returnValue: MutableList<Post> = mutableListOf()
        database.collection("posts").get().addOnCompleteListener {
            when (it.isSuccessful) {
                true -> {
                    val posts: MutableList<Post> = mutableListOf()
                    for (json in it.result) {
                        val id = json.getString("id") ?: ""
                        val title = json.getString("title") ?: ""
                        val description = json.getString("description") ?: ""
                        val category = json.getString("category") ?: ""
                        val image = json.getString("image") ?: ""

                        posts.add(Post(
                            id = id,
                            title = title,
                            description = description,
                            category = category,
                            image = image
                        ))
                    }

                    returnValue = posts
                }
                false -> {}
            }
        }
        return returnValue
    }

    fun add(post: Post) {

        val json = hashMapOf(
            "id" to post.id,
            "title" to post.title,
            "description" to post.description,
            "category" to post.category,
            "image" to post.image,
        )

        database.collection("posts").document(post.id).set(json)
            .addOnCompleteListener {

            }

//        // Create a new user with a first and last name
//        val user = hashMapOf(
//            "first" to "Inbar",
//            "last" to "Lovelace",
//            "born" to 2003,
//        )
//
//// Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w("TAG", "Error adding document", e)
//            }
    }

    fun delete(postId: String) {

    }
}