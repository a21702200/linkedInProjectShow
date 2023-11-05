package pt.ulusofona.deisi.cm2223.g21702200.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filmes")
data class FilmeData(
    @PrimaryKey val id: String,
    val filme: String,
    val cartaz: String,
    val genero: String,
    val sinopse: String,
    val lancamento: String,
    val avaliacao: String,
    val languages: String,
    val awards: String,
    val actors: String,
    val director: String,
    val imdbVotes: String
)