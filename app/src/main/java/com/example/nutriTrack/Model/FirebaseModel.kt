package com.example.nutriTrack.Model

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.nutriTrack.Model.Post.COLLECTION_NAME
import com.example.nutriTrack.Model.Post.Category
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
        database.collection("posts").get().addOnCompleteListener {task ->
            when (task.isSuccessful) {
                true -> {
                    val posts = mutableListOf<Post>()

                    for (document in task.result!!) {

                        val post = Post.createPost(document.data, "123")
                        posts.add(post)
                    }

                    Log.d("Firestore", "After work: ${posts}")

                    returnValue = posts
                    callback(posts)
                }
                false -> {
                    //callback(MutableList())
                    returnValue.add(Post("1234", "Healthy Eating", Category.Nutrition,"Tips for balanced meals","","User1", "3.3.25"))

                }

            }
        }
    }

    fun addPost(post: Post) {

        database.collection(COLLECTION_NAME).document(post.id).set(post.toJson())
            .addOnCompleteListener {
            }

        var postId: String = post.getId()
        if (postId == "") {
            val rootRef = FirebaseFirestore.getInstance()
            val postRef = rootRef.collection(COLLECTION_NAME)
            postId = postRef.document().id
        }

        database.collection(COLLECTION_NAME)
            .document(postId)
            .set(post.toJson())

    }

    fun uploadImage(bitmap: Bitmap, name: String, callback: (String?) -> Unit) {
//        val storageRef: StorageReference = storage.getReference()
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$name.jpg")
//        val imagesRef: StorageReference = storageRef.child("images/$name.jpg")

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

    fun delete(postId: Post, callback: (String?) -> Unit) {

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