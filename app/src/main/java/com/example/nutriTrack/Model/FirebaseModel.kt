package com.example.nutriTrack.Model

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

class FirebaseModel {

    private val database = Firebase.firestore

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
                    returnValue.add(Post("1234", "Healthy Eating", Category.Nutrition,"Tips for balanced meals","","User1", "3.3.25"))

                }

            }
        }
    }

    fun add(post: Post) {
        database.collection(COLLECTION_NAME).document(post.id).set(post.toJson())
            .addOnCompleteListener {

            }

        // Add post case - get new free id
        var postId: String = post.getId()
        if (postId == "") {
            val rootRef = FirebaseFirestore.getInstance()
            val postRef = rootRef.collection(COLLECTION_NAME)
            postId = postRef.document().id
        }

        database.collection(COLLECTION_NAME)
            .document(postId)
            .set(post.toJson())
            .addOnSuccessListener(OnSuccessListener<Void> { unused: Void? ->
//                listener.onComplete(
//                    null
//                )
            })
//            .addOnFailureListener(OnFailureListener { e: Exception? -> listener.onComplete(null) })

    }

    fun delete(postId: String) {
    }


}