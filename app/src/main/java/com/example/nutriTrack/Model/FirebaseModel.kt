package com.example.nutriTrack.Model

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.nutriTrack.base.MyApplication
import com.example.nutriTrack.dao.AppLocalDb
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

import java.io.ByteArrayOutputStream
import java.sql.Timestamp

class FirebaseModel {

    private val database = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth
    private var storage = Firebase.storage

    init {
        val setting = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }

        database.firestoreSettings = setting
        storage = FirebaseStorage.getInstance()
    }

    fun getAllPosts(callback: (MutableList<Post>) -> Unit) {
        var returnValue: MutableList<Post> = mutableListOf()
        database.collection(Post.COLLECTION_NAME).get().addOnCompleteListener { task ->

            when (task.isSuccessful) {
                true -> {
                    val posts = mutableListOf<Post>()

                    for (document in task.result!!) {

                        val post = Post.createPost(document.data)
                        posts.add(post)
                    }

                    Log.d("Firestore", "After work: ${posts}")

                    returnValue = posts
                    callback(posts)
                }
                false -> {
                    //callback(MutableList())
                }

            }
        }
    }

    fun getPostsByUser(userId: String, callback: (MutableList<Post>) -> Unit) {
        database.collection("posts")
            .whereEqualTo("user", userId)
            .get().addOnCompleteListener {task ->
            when (task.isSuccessful) {
                true -> {
                    val posts = mutableListOf<Post>()

                    for (document in task.result!!) {

                        val post = Post.createPost(document.data)
                        posts.add(post)
                    }

                    callback(posts)
                }
                false -> {
                    Toast.makeText(
                        MyApplication.Globals.context,
                        "We couldn't get your posts...",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit = {}) {
        database.collection(Post.COLLECTION_NAME)
            .document(post.id)
            .set(post.toJson())
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun uploadImage(bitmap: Bitmap, name: String, callback: (String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$name.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener(OnFailureListener { callback(null) })
            .addOnSuccessListener(
                OnSuccessListener<Any?> {
                    storageRef.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                        callback(uri.toString())
                    })
                })
    }

    fun getPostById(postId: String, callback: (Post?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("posts").document(postId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val post = document.toObject(Post::class.java)
                    post?.setId(document.id)
                    callback(post)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun deletePost(postId: String, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("posts").document(postId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    db.collection("posts").document(postId).delete()
                        .addOnSuccessListener {
                                callback(true)
                        }
                        .addOnFailureListener {
                            callback(false)
                        }
                } else {
                    callback(false)
                }
            }
            .addOnFailureListener {
                callback(false)
            }
    }
    fun isSignedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun getUserDocumentNumber(): String {
        val currentUser = auth.currentUser
        return currentUser!!.uid
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
                } else {
                    Toast.makeText(
                        MyApplication.Globals.context,
                        "Something went wrong...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun login(email: String?, password: String?, callback: (FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    callback(auth.currentUser)
                } else {
                    Toast.makeText(
                        MyApplication.Globals.context,
                        "Something went wrong...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun addUser(user: User) {
        database.collection(User.COLLECTION_NAME).document(user.id).set(user)
            .addOnCompleteListener {documentReference ->
                Log.d("ADD-USER", "DocumentSnapshot added with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w("ADD-USER", "Error adding document", e)
            }
    }

    fun updateUser(user: User, document: String, callback: (User?) -> Unit) {
        Log.d("UPDATE-USER", "Image url: ${user.imageUrl}")

        database.collection(User.COLLECTION_NAME).document(document)
            .update("name", user.name, "phone", user.phone, "imageUrl", user.imageUrl)
            .addOnSuccessListener {task ->
                Log.d("TAG", "DocumentSnapshot updated with ID: ${task}")
                callback(user)
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    fun getUserInfoByEmail(email: String, callback: (User?) -> Unit) {
        database.collection(User.COLLECTION_NAME)
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { task ->
                if (!task.isEmpty) {
                    val document = task.documents[0]
                    val user = document.toObject(User::class.java)
                    Log.d("userInfo", "user: $user")
                    callback(user)
                } else {
                    callback(null) // No user found
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseModel", "Error getting user info: ${exception.message}")
                callback(null)
            }
    }
}