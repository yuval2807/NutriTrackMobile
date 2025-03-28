package com.example.nutriTrack.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Post
import kotlinx.coroutines.launch

class PostsListViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    fun fetchPosts() {
        viewModelScope.launch {
            val firebaseModel = FirebaseModel()
            firebaseModel.getAllPosts { fetchedPosts ->
                _posts.value = fetchedPosts
            }
        }
    }
}