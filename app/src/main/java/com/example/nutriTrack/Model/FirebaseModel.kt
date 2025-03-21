package com.example.nutriTrack.Model

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.nutriTrack.Model.Post.COLLECTION_NAME
import com.example.nutriTrack.Model.Post.Category
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

import java.io.ByteArrayOutputStream

class FirebaseModel {

    private val database = Firebase.firestore
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

}