package com.example.nutriTrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriTrack.Model.Post
import com.example.nutriTrack.utils.loadImageIntoImageView
import com.squareup.picasso.Picasso


interface OnItemClickListener {
    fun onItemClick(v: View?, position: Int)
}
class PostListAdapter(private var data: List<Post>) :
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
        return PostsListViewHolder(view, listener, data)
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
    private var data: List<Post>?
) : RecyclerView.ViewHolder(itemView) {

    private val titleTv: TextView = itemView.findViewById(R.id.tv_postlist_post_title)
    private val descriptionTv: TextView = itemView.findViewById(R.id.tv_postlist_post_description)
    private val categoryTv: TextView = itemView.findViewById(R.id.tv_postlist_post_category)
    private val dateTv: TextView = itemView.findViewById(R.id.tv_postlist_date)
    private val imageView: ImageView = itemView.findViewById(R.id.iv_postlist_post_image)


    init {
        itemView.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                listener?.onItemClick(it, pos)
            }
        }
    }

    fun bind(post: Post) {
        titleTv.text = post.getTitle()
        descriptionTv.text = post.getDescription()
        categoryTv.text = post.getCategory().name
        dateTv.text = post.date
        loadImageIntoImageView(imageView,post.imageUrl)

        // Uncomment this part when you have image loading logic:
        // if (!post.getImageUrl().isNullOrEmpty()) {
        //     Picasso.get().load(post.getImageUrl()).into(image)
        // } else {
        //     image.setImageResource(R.drawable.no_image)
        // }
    }
}
