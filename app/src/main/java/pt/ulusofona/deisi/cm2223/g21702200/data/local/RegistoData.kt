package pt.ulusofona.deisi.cm2223.g21702200.data.local


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "registo")
data class RegistoData(
    @PrimaryKey val uuid: String,
    val filmeId: String,
    val cinemaId: Int,
    val name: String,
    val cinema: String,
    val rate: String,
    val date: Long,
    val observacoes: String,
    val postCode: String
)

@Entity(tableName = "photosRegisto")
data class PhotoDataRegistration(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "photo_id") val id: Int,
    val registoUuid: String,
    val dataPhoto: String,
    val Timestamp: Long
)


