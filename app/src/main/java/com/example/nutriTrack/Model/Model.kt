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
//    val posts: LiveData<List<Post>> = database.postDao().getAllPost()

    private var executor = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    private val firebaseModel = FirebaseModel()
    private val cloudinaryModel = CloudinaryModel()
    companion object {
        val shared = Model()
    }

    fun addPost(post: Post, image: Bitmap?, storage: Storage, callback: (String?) -> Unit) {
        firebaseModel.addPost(post)
            image?.let {
                uploadTo(
                    storage,
                    image = image,
                    name = post.id,
                    callback = { uri ->
                        if (!uri.isNullOrBlank()) {
                            post.setImageUrl(uri)

                            firebaseModel.addPost(post)
                        } else {
                            callback("")
                        }
                    },
                )
            } ?: callback("")

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
        firebaseModel.deletePost(postId, callback)
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
        cloudinaryModel.uploadImage(
            bitmap = bitmap,
            name = name,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun refreshAllPosts() {
        firebaseModel.getAllPosts  { posts ->
            executor.execute {
                Log.d("getAllPosts", "After work: ${posts}")

                for (post in posts) {
                    Log.d("getAllPosts insert", "After work: ${post}")

                    database.postDao().insertAll(post)

                }
            }
        }
    }
}