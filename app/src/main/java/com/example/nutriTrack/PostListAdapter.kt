package com.example.nutriTrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Post
import com.example.nutriTrack.databinding.PostsListRowBinding

interface OnItemClickListener {
    fun onItemClick(v: View?, position: Int)
}

class PostListAdapter(private var posts: List<Post>?, private val navController: NavController) :
    RecyclerView.Adapter<PostsListViewHolder>() {

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setData(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsListViewHolder {
        val binding = PostsListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsListViewHolder(binding, listener, posts, navController)
    }

    override fun onBindViewHolder(holder: PostsListViewHolder, position: Int) {
        val post = posts?.get(position)
        if (post != null) {
            holder.bind(post)
        }
    }

    fun removeItem(position: Int) {
        if(posts!=null){
            if (position >= 0 && position < posts!!.size) {
                val mutableData = posts!!.toMutableList()
                mutableData.removeAt(position)
                posts = mutableData
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int = if(posts!=null){ posts!!.size} else {0}
}

class PostsListViewHolder(
    private val binding: PostsListRowBinding,
    private val listener: OnItemClickListener?,
    private var data: List<Post>?,
    private val navController: NavController
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                listener?.onItemClick(it, pos)
            }
        }

        binding.btnEdit.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION && data != null) {
                val post = data!![pos]

                val bundle = Bundle().apply {
                    putString("postId", post.getId())
                }
                navController.navigate(R.id.action_homeFragment_to_addNewPost, bundle)
            }
        }

        binding.btnDelete.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION && data != null) {
                val post = data!![pos]
                showDeleteConfirmation(post)
            }
        }
    }

    fun bind(post: Post) {
        binding.tvPostlistPostTitle.text = post.getTitle()
        binding.tvPostlistPostDescription.text = post.getDescription()
        binding.tvPostlistPostCategory.text = post.getCategory().name
        binding.tvPostlistDate.text = post.date
        loadImageIntoImageView(binding.ivPostlistPostImage, post.imageUrl)
    }

    private fun showDeleteConfirmation(post: Post) {
        val context = itemView.context
        AlertDialog.Builder(context)
            .setTitle("Delete Post")
            .setMessage("Are you sure you want to delete this post?")
            .setPositiveButton("Delete") { _, _ ->
                val firebaseModel = FirebaseModel()
                firebaseModel.deletePost(post.getId()) { success ->
                    if (success) {
                        if (position != RecyclerView.NO_POSITION) {
                            (bindingAdapter as? PostListAdapter)?.removeItem(position)
                        }
                        Toast.makeText(context, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to delete post", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}