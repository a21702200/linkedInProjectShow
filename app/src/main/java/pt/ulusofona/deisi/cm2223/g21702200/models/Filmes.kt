package pt.ulusofona.deisi.cm2223.g21702200.models

//Filmes vindos da Api

data class Filmes(
    val id: String,
    val filme: String, // nome
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