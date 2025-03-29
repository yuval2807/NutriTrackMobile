package com.example.nutriTrack.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nutriTrack.Model.Model
import com.example.nutriTrack.Model.Post

class PostsListViewModel : ViewModel() {

    fun getPosts(userId: String?): LiveData<List<Post>> {
        return if (userId == null) {
           Model.shared.getAllPosts()
        } else {
            Model.shared.getPostsByUserId(userId)
        }
    }

    fun refreshAllPost() {
        Model.shared.refreshAllPosts()
    }
}