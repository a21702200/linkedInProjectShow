package pt.ulusofona.deisi.cm2223.g21702200.models

//Registos


data class RegistrationData(
    val uuid: String,
    val filmeId: String,
    val cinemaId: Int,
    val name: String,
    val cinema: String,
    val rate: String,
    val date: Long,
    val imageUris: List<Photo>?,
    val observacoes: String,
    val postcode: String
)

data class Photo(
    val dataPhoto: String, val Timestamp: Long
)
