package  com.example.nutriTrack.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutriTrack.Model.Post

@Dao
interface PostDao {

    @Query("SELECT * FROM Post")
    fun getAllPost(): LiveData<List<Post>>

    @Query("SELECT * FROM Post WHERE id =:id")
    fun getPostById(id: String): Post

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg post: Post)

    @Query("DELETE FROM Post WHERE id = :postId")
    fun deletePostById(postId: String)
}