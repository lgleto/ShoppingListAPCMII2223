package ipca.example.shoppinglist

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Entity
data class Item (
    @PrimaryKey
    var reference : String,
    var description : String,
    var date : Date,
    var qtd : Double
    )

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): LiveData<List<Item>>

    @Query("SELECT * FROM item WHERE reference=:reference")
    fun loadByReference(reference: String): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)
}