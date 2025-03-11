package com.example.nutriTrack;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.nutriTrack.Model.Post;

import java.util.List;

public class PostsListViewModel extends ViewModel {

    public enum ListMode {
        AllPosts,
        MyPosts
    }

    LiveData<List<Post>> data;
    ListMode mode;

    public PostsListViewModel(ListMode listMode) {
        mode = listMode;

//        if (listMode == ListMode.AllPosts) {
//            data = Model.instance().getAllMaslulim();
//        } else {
//            data = Model.instance().getMyMaslulim();  // TODO: add model
//        }
    }

    public LiveData<List<Post>> getData() {
        return data;
    }

    public ListMode getMode() {
        return this.mode;
    }
}
