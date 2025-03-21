package com.example.nutriTrack.Model

import android.util.Log
import android.widget.Toast
import com.example.nutriTrack.Model.Post.COLLECTION_NAME
import com.example.nutriTrack.Model.Post.Category
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel {

    private val database = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth


    init {
        val setting = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }

        database.firestoreSettings = setting
    }

    fun getAllPosts(callback: (MutableList<Post>) -> Unit) {
        var returnValue: MutableList<Post> = mutableListOf()
        database.collection("posts").get().addOnCompleteListener {task ->
            when (task.isSuccessful) {
                true -> {
                    val posts = mutableListOf<Post>()

                    for (document in task.result!!) {

                        val post = Post.createPost(document.data, "123")
                        posts.add(post)
                    }

                    Log.d("Firestore", "After work: ${posts[0]}")

                    returnValue = posts
                    callback(posts)
                }
                false -> {
                    //callback(MutableList())
                    returnValue.add(Post("User1", "Healthy Eating", Category.Nutrition,"Tips for balanced meals"))

                }

            }
        }
    }

    fun add(post: Post) {

        database.collection(COLLECTION_NAME).document(post.id).set(post.toJson())
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

    fun isSignedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun getUserEmail(): String? {
        val currentUser = auth.currentUser
        return currentUser!!.email
    }

    fun register(email: String?, password: String?, callback: (FirebaseUser?) -> Unit) {
        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    callback(auth.currentUser)
                    //  listener.onComplete(auth.currentUser)
                } else {
//                    Toast.makeText(
//                        MyApplication.getContext(), task.exception!!.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                 //   listener.onComplete(null)
                }
            }
    }

    fun login(email: String?, password: String?, callback: (FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    callback(auth.currentUser)
                } else {
//                    Toast.makeText(
//                        //MyApplication.getContext(), task.exception!!.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                   // listener.onComplete(null)
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
}