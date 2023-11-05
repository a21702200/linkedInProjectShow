package pt.ulusofona.deisi.cm2223.g21702200.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RegistoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(registoData: RegistoData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPhotosRegistration(photo: PhotoDataRegistration)

    @Query("SELECT * FROM registo")
    fun getAll(): List<RegistoData>

    @Query("SELECT * FROM registo WHERE name = :name")
    fun getAllByName(name: String): List<RegistoData>

    @Query("DELETE FROM registo")
    fun deleteAll()

    @Query("SELECT * FROM registo WHERE uuid = :uuid")
    fun getById(uuid: String): RegistoData

    @Query("SELECT * FROM photosRegisto WHERE registoUuid = :id")
    fun getPhotosByUuid(id: String): List<PhotoDataRegistration>


}

