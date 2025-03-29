    package  com.example.nutriTrack.dao

    import android.content.Context
    import androidx.room.Database
    import androidx.room.Room
    import androidx.room.RoomDatabase
    import com.example.nutriTrack.Model.Post
    import com.example.nutriTrack.base.MyApplication

    @Database(entities = [Post::class], version = 1)
    abstract class AppLocalDbRepository: RoomDatabase() {
        abstract fun postDao(): PostDao
    }

    object AppLocalDb {

        private const val LOCAL_LAST_UPDATE = "last_update_date"

        val database: AppLocalDbRepository by lazy {

            val context = MyApplication.Globals.context ?: throw IllegalStateException("Application context is missing")

            Room.databaseBuilder(
                context = context,
                klass = AppLocalDbRepository::class.java,
                name = "dbName.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        fun saveLastUpdateDate(timestamp: Long) {
            val context = MyApplication.Globals.context ?: throw IllegalStateException("Application context is missing")
            val sharedPreferences = context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
            sharedPreferences.edit().putLong(LOCAL_LAST_UPDATE, timestamp).apply()
        }

        fun getLastUpdateDate(): Long {
            val context = MyApplication.Globals.context ?: throw IllegalStateException("Application context is missing")
            val sharedPreferences = context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
            return sharedPreferences.getLong(LOCAL_LAST_UPDATE, 0)
        }
    }