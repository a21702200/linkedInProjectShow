package pt.ulusofona.deisi.cm2223.g21702200.models

data class Cinema(
    val cinema_id: Int,
    val cinema_name: String,
    val cinema_provider: String,
    val logo_url: String?,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val postcode: String,
    val county: String,
    val photos: List<String>?,
    val ratings: List<CinemaRating>,
    val opening_hours: CinemaDays
)

data class CinemaRating(
    val category: String, val score: Int
)

data class CinemaDays(
    val Monday: CinemaOpeningHours,
    val Tuesday: CinemaOpeningHours,
    val Wednesday: CinemaOpeningHours,
    val Thursday: CinemaOpeningHours,
    val Friday: CinemaOpeningHours,
    val Saturday: CinemaOpeningHours,
    val Sunday: CinemaOpeningHours
)

data class CinemaOpeningHours(
    val open: Long, val close: Long
)


