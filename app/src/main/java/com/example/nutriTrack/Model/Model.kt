package com.example.nutriTrack.Model

import android.graphics.Bitmap
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import androidx.lifecycle.LiveData
import com.example.nutriTrack.dao.AppLocalDb
import com.example.nutriTrack.dao.AppLocalDbRepository
import java.util.concurrent.Executors

class Model private constructor() {

    enum class Storage {
        FIREBASE,
        CLOUDINARY
    }

    private val database: AppLocalDbRepository = AppLocalDb.database
    val posts: LiveData<List<Post>> = database.postDao().getAllPost()

    private var executor = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    private val firebaseModel = FirebaseModel()
    private val cloudinaryModel = CloudinaryModel()
    companion object {
        val shared = Model()
    }

    fun addPost(post: Post, image: Bitmap?, storage: Storage, callback: (String?) -> Unit) {
        if (image != null) {
            uploadTo(
                storage,
                image = image,
                name = post.id,
                callback = { uri ->
                    if (!uri.isNullOrBlank()) {
                        post.setImageUrl(uri)

                        firebaseModel.addPost(post) { success ->
                            // Assuming firebaseModel.addPost can take a callback for completion
                            if (success) {
                                refreshAllPosts()
                            }
                            callback(if (success) post.id else "")
                        }
                    } else {
                        callback("")
                    }
                }
            )
        }
        else {
            // Handle case with no image
            firebaseModel.addPost(post) { success ->
                if (success) {
                    refreshAllPosts()
                }
                callback(if (success) post.id else "")
            }
        }
    }

    fun addUser(user: User, image: Bitmap?, storage: Storage, callback: (String?) -> Unit) {
        if (image != null) {
            uploadTo(
                storage,
                image = image,
                name = user.id,
                callback = { uri ->
                    if (!uri.isNullOrBlank()) {
                        user.imageUrl = uri

                        firebaseModel.addUser(user) { success ->
                            callback(if (success) user.id else "")
                        }
                    } else {
                        callback("")
                    }
                }
            )
        }
        else {
            firebaseModel.addUser(user) { success ->
                callback(if (success) user.id else "")
            }
        }
    }

    private fun uploadTo(storage: Storage, image: Bitmap, name: String, callback: (String?) -> Unit) {
        when (storage) {
            Storage.FIREBASE -> {
                uploadImageToFirebase(image, name, callback)
            }
            Storage.CLOUDINARY -> {
                uploadImageToCloudinary(
                    bitmap = image,
                    name = name,
                    onSuccess = callback,
                    onError = { callback(null) }
                )
            }
        }
    }

    fun deletePost(postId: String, callback: (Boolean) -> Unit) {
        firebaseModel.deletePost(postId) { success ->
            if (success) {
                executor.execute {
                    database.postDao().deletePostById(postId)
                }
            }
            callback(success)
        }
    }

    private fun uploadImageToFirebase(
        image: Bitmap,
        name: String,
        callback: (String?) -> Unit
    ) {
        firebaseModel.uploadImage(image, name, callback)
    }

    private fun uploadImageToCloudinary(
        bitmap: Bitmap,
        name: String,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) {
        Log.d("Image", "uploadImageToCloudinary:  name ${name} , bitmap:${bitmap}")

        cloudinaryModel.uploadImage(
            bitmap = bitmap,
            name = name,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun getAllPosts(): LiveData<List<Post>> {
        refreshAllPosts()
        return posts
    }

    fun getPostsByUserId(userId: String): LiveData<List<Post>> {
        refreshAllPosts()
        return database.postDao().getPostsByUserId(userId)
    }

    fun refreshAllPosts() {
        val lastLocalUpdate = AppLocalDb.getLastUpdateDate() // Get last local update date
        Log.d("getAllPosts insert", "lastLocalUpdate: ${lastLocalUpdate}")

        firebaseModel.getAllPosts  { posts ->
            executor.execute {
                Log.d("getAllPosts", "After work: ${posts}")

                // Filter posts based on last update
                val newPosts = posts.filter { post ->
                    val postLastUpdate = post.getLastUpdated()
                    Log.d("getAllPosts insert", "post last Update: ${postLastUpdate}")

                    postLastUpdate == null || postLastUpdate > lastLocalUpdate
                }

                if (newPosts.isNotEmpty()) {
                    for (post in newPosts) {
                        Log.d("getAllPosts insert", "After work: ${post.title}")

                        database.postDao().insertAll(post)
                    }

                    // Update last local update date
                    val latestUpdate = newPosts.maxOf { it.getLastUpdated() }
                    AppLocalDb.saveLastUpdateDate(latestUpdate)
                    Log.d("refreshAllPosts", "Last update date saved: $latestUpdate")
                }
            }
        }
    }
}