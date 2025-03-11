package com.example.nutriTrack.Model

data class Post(
    var id: String,
    var title: String,
    var description: String,
    var category:String,
    var image: String,

    ) {
    companion object {
        private var counter = 0

        private val firebaseModel = FirebaseModel()

        // Static list to store students (our in-memory "database")
        private var postsList = mutableListOf<Post>()

        fun getAllStudents(): List<Post> {
            postsList = firebaseModel.getAllPosts()
            return postsList
        }

        fun addPost(post: Post) {
            firebaseModel.add(post)
            postsList.add(post)
        }

        fun updatePost(studentId:String, updatedStudent: Post) {
            val index = postsList.indexOfFirst { it.id == studentId }
            if (index != -1) {
                postsList[index] = updatedStudent
            }
        }

        fun deletePost(postId: String) {
            firebaseModel.delete(postId)
            postsList.removeIf { it.id == postId }
        }

        fun getPostById(postId: String): Post? {
            return postsList.find { it.id == postId }
        }
    }
}