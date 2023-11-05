package pt.ulusofona.deisi.cm2223.g21702200.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CinemasDao {

    @Insert
    fun insert(cinemasData: CinemasData)

    @Insert
    fun insertPhotos(photos: Photos)

    @Insert
    fun insertRating(rating: Rating)

    @Insert
    fun insertOpeningHours(openingHours: OpeningHours)

    @Query("SELECT * FROM cinemas")
    fun getAll(): List<CinemasData>

    @Query("SELECT * FROM opening_hours WHERE cinemaId = :id")
    fun getAllHours(id: Int): List<OpeningHours>

    @Query("SELECT * FROM opening_hours WHERE cinemaId = :id")
    fun getAllHoursFromCinema(id: Int): OpeningHours

    @Query("SELECT cinema_id FROM cinemas WHERE cinema_name = :name")
    fun getCinemaId(name: String): Int

    @Query("SELECT * FROM cinemas WHERE cinema_id = :id")
    fun getCinemaById(id: Int): CinemasData

    @Query("SELECT * FROM photosCinema WHERE cinemaId = :id")
    fun getPhotos(id: Int): List<Photos>

    @Query("SELECT * FROM ratings WHERE cinemaId = :id")
    fun getRatings(id: Int): List<Rating>

    @Query("DELETE FROM cinemas")
    fun deleteAll()

    @Query("DELETE FROM photosCinema")
    fun deleteAllPhotos()

    @Query("DELETE FROM ratings")
    fun deleteAllRating()

    @Query("DELETE FROM opening_hours")
    fun deleteAllOpeningHours()

    @Query("SELECT * FROM cinemas WHERE cinema_name = :name")
    fun getCinemaByName(name: String): CinemasData

}