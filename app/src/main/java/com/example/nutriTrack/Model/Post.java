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
    String user;
    String date;

    public Post(@NonNull String id, String title, Category category, String description, String imageUrl,String user, String date) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.imageUrl= imageUrl;
        this.isDeleted = false;
        this.user = user;
        this.date = date;
    }

    public Post() {
        this.id = "";
        this.title = "";
        this.category = Category.Nutrition;
        this.description = "";
        this.isDeleted = false;
        this.user = "";
        this.date = "";
    }

    public enum Category {
        Nutrition,
        Sports,
        Lifestyle,
        Health
    }

    public static Post createPost(Map<String, Object> postJson) {
        String id = (String) postJson.get("id");
        String title = (String) postJson.get("title");
        Category category = Category.valueOf((String) postJson.get("category"));
        String description = (String) postJson.get("description");
        String imageUrl = (String) postJson.get("imageUrl");
        Boolean isDeleted = (Boolean) postJson.get("isDeleted");
        String user = (String) postJson.get("user");
        String date = (String) postJson.get("date");

        Post postItem = new Post(id,title,category,description, imageUrl, user,date);

        postItem.setImageUrl(imageUrl);
        postItem.setId(id);
        postItem.setDeleted(isDeleted);
        return postItem;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("title", title);
        json.put("category", category);
        json.put("description", description);
        json.put("imageUrl", imageUrl);
        json.put("isDeleted", isDeleted);
        json.put("user", user);
        json.put("date", date);

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
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        this.isDeleted = deleted;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
