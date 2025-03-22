package com.example.nutriTrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriTrack.Model.Post
import com.squareup.picasso.Picasso


interface OnItemClickListener {
    fun onItemClick(v: View?, position: Int)
}

class PostListAdapter(private var data: List<Post>, private val navController: NavController) :
    RecyclerView.Adapter<PostsListViewHolder>() {

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setData(newData: List<Post>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.posts_list_row, parent, false)
        return PostsListViewHolder(view, listener, data, navController)
    }

    override fun onBindViewHolder(holder: PostsListViewHolder, position: Int) {
        val post = data[position]
        holder.bind(post)

    }

    override fun getItemCount(): Int = data.size
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

    }

    fun bind(post: Post) {
        titleTv.text = post.getTitle()
        descriptionTv.text = post.getDescription()
        categoryTv.text = post.getCategory().name
        dateTv.text = post.date
        loadImageIntoImageView(imageView,post.imageUrl)

    }
}
