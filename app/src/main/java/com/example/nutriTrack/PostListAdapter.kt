package com.example.nutriTrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriTrack.Model.FirebaseModel
import com.example.nutriTrack.Model.Post


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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.posts_list_row, parent, false)
        return PostsListViewHolder(view, listener, posts, navController)
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
    itemView: View,
    private val listener: OnItemClickListener?,
    private var data: List<Post>?,
    private val navController: NavController
) : RecyclerView.ViewHolder(itemView) {

    private val titleTv: TextView = itemView.findViewById(R.id.tv_postlist_post_title)
    private val descriptionTv: TextView = itemView.findViewById(R.id.tv_postlist_post_description)
    private val categoryTv: TextView = itemView.findViewById(R.id.tv_postlist_post_category)
    private val dateTv: TextView = itemView.findViewById(R.id.tv_postlist_date)
    private val imageView: ImageView = itemView.findViewById(R.id.iv_postlist_post_image)
    private val editButton: ImageButton = itemView.findViewById(R.id.btn_edit)
    private val deleteButton: ImageButton = itemView.findViewById(R.id.btn_delete)


    init {
        itemView.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                listener?.onItemClick(it, pos)
            }
        }

        editButton.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION && data != null) {
                val post = data!![pos]

                val bundle = Bundle().apply {
                    putString("postId", post.getId())
                }
                navController.navigate(R.id.action_postsFragment_to_addNewPost,bundle )
            }
        }

        deleteButton.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION && data != null) {
                val post = data!![pos]
                showDeleteConfirmation(post)
            }
        }

    }

    fun bind(post: Post) {
        titleTv.text = post.getTitle()
        descriptionTv.text = post.getDescription()
        categoryTv.text = post.getCategory().name
        dateTv.text = post.date
        loadImageIntoImageView(imageView,post.imageUrl)

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
