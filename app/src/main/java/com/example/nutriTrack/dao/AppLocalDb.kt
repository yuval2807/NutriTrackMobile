    package  com.example.nutriTrack.dao

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
    }