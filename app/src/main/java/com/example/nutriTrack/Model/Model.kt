package com.example.nutriTrack.Model

import android.graphics.Bitmap

class Model private constructor() {


    enum class Storage {
        FIREBASE,
        CLOUDINARY
    }

    private val firebaseModel = FirebaseModel()
    private val cloudinaryModel = CloudinaryModel()
    companion object {
        val shared = Model()
    }

    fun add(post: Post, image: Bitmap?, storage: Storage, callback: (String?) -> Unit) {
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

    fun addUser(user: User, image: Bitmap?, storage: Storage, callback: (String?) -> Unit) {
        firebaseModel.addUser(user)
        image?.let {
            uploadTo(
                storage,
                image = image,
                name = user.id,
                callback = { uri ->
                    if (!uri.isNullOrBlank()) {
                        user.imageUrl = uri

                        firebaseModel.addUser(user)
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

    fun delete(post: Post, callback: (String?) -> Unit) {
        firebaseModel.delete(post, callback)
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

}