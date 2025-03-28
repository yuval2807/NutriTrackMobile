package com.example.nutriTrack.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nutriTrack.Model.Model
import com.example.nutriTrack.Model.Post

class PostsListViewModel : ViewModel() {
    var posts: LiveData<List<Post>> = Model.shared.posts

    fun refreshAllPost() {
        Model.shared.refreshAllPosts()
    }
}