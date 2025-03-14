package com.example.nutriTrack.Model;

import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;

//@Entity
public class Post {
    public static final String COLLECTION_NAME = "posts";

//    @PrimaryKey
    @NonNull
    String id;
    String title;
    Category category;
    String description;
    String imageUrl;
    Boolean isDeleted;

    public Post(@NonNull String id, String title, Category dif, String description) {
        this.id = id;
        this.title = title;
        this.category = dif;
        this.description = description;
        this.isDeleted = false;
    }

    public Post() {
        this.id = "";
        this.title = "";
        this.category = Category.Nutrition;
        this.description = "";
        this.isDeleted = false;
    }

    public enum Category {
        Nutrition,
        Sports,
        Lifestyle,
        Health
    }

    public static Post createPost(Map<String, Object> postJson, String docId) {
        String title = (String) postJson.get("title");
        Category category = Category.valueOf((String) postJson.get("category"));
        String description = (String) postJson.get("description");
        String imageUrl = (String) postJson.get("imageUrl");
        Boolean isDeleted = (Boolean) postJson.get("isDeleted");


        Post postItem = new Post(docId,title,category,description);

        postItem.setImageUrl(imageUrl);
        postItem.setId(docId);
        postItem.setDeleted(isDeleted);
        return postItem;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("title", title);
        json.put("difficulty", category);
        json.put("description", description);
        json.put("imageUrl", imageUrl);
        json.put("isDeleted", isDeleted);

        return json;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
