import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nutriTrack.Model.Post
import com.example.nutriTrack.Model.Post.Category
import com.example.nutriTrack.PostListAdapter
import com.example.nutriTrack.R
class PostsFragment : Fragment() {

    private lateinit var postListAdapter: PostListAdapter
    private lateinit var postList: MutableList<Post>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.postsList_rv)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.postsListRv_swipeRefresh)

        postList = mutableListOf()
        postListAdapter = PostListAdapter(postList)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postListAdapter

        swipeRefreshLayout.setOnRefreshListener {
            loadPosts()
            swipeRefreshLayout.isRefreshing = false
        }

        loadPosts()

        return view
    }

    private fun loadPosts() {
        postList.clear()
        postList.add(Post("User1", "Healthy Eating", Category.Nutrition,"Tips for balanced meals"))
        postList.add(Post("User2", "Workout Routines", Category.Sports,"Best exercises for strength"))
        postList.add(Post("User3", "Workout Routines", Category.Sports,"Best exercises for strength"))
        postList.add(Post("User4", "Workout Routines", Category.Sports,"Best exercises for strength"))
        postListAdapter.setData(postList)
    }
}
