package pt.ulusofona.deisi.cm2223.g21702200.data.local


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cinemas")
data class CinemasData(
    @PrimaryKey @ColumnInfo(name = "cinema_id") val id: String,
    @ColumnInfo(name = "cinema_name") val name: String,
    @ColumnInfo(name = "cinema_provider") val provider: String,
    @ColumnInfo(name = "logo_url") val logo: String?,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val postcode: String,
    val county: String
)

@Entity(tableName = "photosCinema")
data class Photos(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "photos_id") val id: Int,
    val cinemaId: Int,  // Foreign key to link with CinemasData
    val photoUrl: String
)


@Entity(tableName = "ratings")
data class Rating(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ratings_id") val id: Int,
    val cinemaId: Int,  // Foreign key to link with CinemasData
    val category: String,
    val score: Int
)

@Entity(tableName = "opening_hours")
data class OpeningHours(
    @PrimaryKey val cinemaId: Int,  // Foreign key to link with CinemasData
    val mondayOpen: Long,
    val mondayClose: Long,
    val tuesdayOpen: Long,
    val tuesdayClose: Long,
    val wednesdayOpen: Long,
    val wednesdayClose: Long,
    val thursdayOpen: Long,
    val thursdayClose: Long,
    val fridayOpen: Long,
    val fridayClose: Long,
    val saturdayOpen: Long,
    val saturdayClose: Long,
    val sundayOpen: Long,
    val sundayClose: Long
)
