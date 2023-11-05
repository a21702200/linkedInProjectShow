package pt.ulusofona.deisi.cm2223.g21702200.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilmesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(filmeData: FilmeData)

    @Query("SELECT * FROM filmes")
    fun getAll(): List<FilmeData>

    @Query("SELECT * FROM filmes WHERE id = :id")
    fun getAllById(id: String): List<FilmeData>

    @Query("SELECT * FROM filmes WHERE id = :id")
    fun getById(id: String): FilmeData

    @Query("DELETE FROM filmes")
    fun deleteAll()

}