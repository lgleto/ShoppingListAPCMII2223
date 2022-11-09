package ipca.example.shoppinglist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Item::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()  {

    abstract fun itemDao(): ItemDao

    companion object {

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase? {
            if(INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java, "db_items"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

}