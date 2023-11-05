package pt.ulusofona.deisi.cm2223.g21702200.data.local


import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.models.Cinema
import pt.ulusofona.deisi.cm2223.g21702200.models.CinemaDays
import pt.ulusofona.deisi.cm2223.g21702200.models.CinemaOpeningHours
import pt.ulusofona.deisi.cm2223.g21702200.models.CinemaRating
import pt.ulusofona.deisi.cm2223.g21702200.models.Filmes
import pt.ulusofona.deisi.cm2223.g21702200.models.FilmesModel
import pt.ulusofona.deisi.cm2223.g21702200.models.Photo
import pt.ulusofona.deisi.cm2223.g21702200.models.ProjectData
import pt.ulusofona.deisi.cm2223.g21702200.models.RegistrationData

class ProjectRoom(
    private val cinemasDao: CinemasDao,
    private val filmesDao: FilmesDao,
    private val registoDao: RegistoDao,
) : ProjectData() {

    override fun getFilmesByName(
        searchName: String, onFinished: (Result<List<FilmesModel>>) -> Unit
    ) {
        Log.e("APP", "ProjectRoom method can’t getFilmesByName")
    }

    override fun getFilmeById(id: String, onFinished: (Result<Filmes>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val filme = filmesDao.getById(id)

            if (filme != null) {

                onFinished(
                    Result.success(
                        Filmes(
                            filme.id,
                            filme.filme,
                            filme.cartaz,
                            filme.genero,
                            filme.sinopse,
                            filme.lancamento,
                            filme.avaliacao,
                            filme.languages,
                            filme.awards,
                            filme.actors,
                            filme.director,
                            filme.imdbVotes
                        )
                    )
                )
            }
        }
    }

    override fun getAllFilmes(
        onFinished: (Result<List<Filmes>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            val filmes = filmesDao.getAll().map {
                Filmes(
                    it.id,
                    it.filme,
                    it.cartaz,
                    it.genero,
                    it.sinopse,
                    it.lancamento,
                    it.avaliacao,
                    it.languages,
                    it.awards,
                    it.actors,
                    it.director,
                    it.imdbVotes
                )
            }

            onFinished(Result.success(filmes))
        }
    }

    override fun insertFilmes(filmes: Filmes, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            val filme = FilmeData(
                filmes.id,
                filmes.filme,
                filmes.cartaz,
                filmes.genero,
                filmes.sinopse,
                filmes.lancamento,
                filmes.avaliacao,
                filmes.languages,
                filmes.awards,
                filmes.actors,
                filmes.director,
                filmes.imdbVotes
            )

            filmesDao.insert(filme)

            Log.i("APP", "Inserted $filme in DB")
        }
        onFinished()
    }


    override fun insertCinemas(cinemas: List<Cinema>) {

        cinemasDao.deleteAll()
        cinemasDao.deleteAllPhotos()
        cinemasDao.deleteAllRating()
        cinemasDao.deleteAllOpeningHours()

        CoroutineScope(Dispatchers.IO).launch {

            for (cinema in cinemas) {

                val cinemaToInsert = CinemasData(
                    cinema.cinema_id.toString(),
                    cinema.cinema_name,
                    cinema.cinema_provider,
                    cinema.logo_url,
                    cinema.latitude,
                    cinema.longitude,
                    cinema.address,
                    cinema.postcode,
                    cinema.county
                )

                cinemasDao.insert(cinemaToInsert)

                if (cinema.photos != null) {
                    for (cinemaPhoto in cinema.photos) {
                        cinemasDao.insertPhotos(Photos(0, cinema.cinema_id, cinemaPhoto))
                    }
                }

                for (rating in cinema.ratings) {
                    cinemasDao.insertRating(
                        Rating(
                            0, cinema.cinema_id, rating.category, rating.score
                        )
                    )
                }


                cinemasDao.insertOpeningHours(
                    OpeningHours(
                        cinema.cinema_id,
                        cinema.opening_hours.Monday.open,
                        cinema.opening_hours.Monday.close,
                        cinema.opening_hours.Tuesday.open,
                        cinema.opening_hours.Tuesday.close,
                        cinema.opening_hours.Wednesday.open,
                        cinema.opening_hours.Wednesday.close,
                        cinema.opening_hours.Thursday.open,
                        cinema.opening_hours.Thursday.close,
                        cinema.opening_hours.Friday.open,
                        cinema.opening_hours.Friday.close,
                        cinema.opening_hours.Saturday.open,
                        cinema.opening_hours.Saturday.close,
                        cinema.opening_hours.Sunday.open,
                        cinema.opening_hours.Sunday.close
                    )
                )


                Log.i("APP", "Inserted $cinema in DB")
            }
        }
    }

    override fun getAllCinemas(onFinished: (Result<List<Cinema>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            val cinemas = cinemasDao.getAll()

            val cinemasToGive = mutableListOf<Cinema>()

            for (cinema in cinemas) {

                val cinemaHours = cinemasDao.getAllHoursFromCinema(cinema.id.toInt())

                val cinemaPhotos = cinemasDao.getPhotos(cinema.id.toInt()).map { it.photoUrl }

                val cinemaRatings = cinemasDao.getRatings(cinema.id.toInt())
                    .map { CinemaRating(it.category, it.score) }

                cinemasToGive.add(
                    Cinema(
                        cinema.id.toInt(),
                        cinema.name,
                        cinema.provider,
                        cinema.logo,
                        cinema.latitude,
                        cinema.longitude,
                        cinema.address,
                        cinema.postcode,
                        cinema.county,
                        cinemaPhotos,
                        cinemaRatings,
                        CinemaDays(
                            CinemaOpeningHours(
                                cinemaHours.mondayOpen,
                                cinemaHours.mondayClose,
                            ), CinemaOpeningHours(
                                cinemaHours.tuesdayOpen, cinemaHours.tuesdayClose
                            ), CinemaOpeningHours(
                                cinemaHours.wednesdayOpen, cinemaHours.wednesdayClose
                            ), CinemaOpeningHours(
                                cinemaHours.thursdayOpen, cinemaHours.thursdayClose
                            ), CinemaOpeningHours(
                                cinemaHours.fridayOpen, cinemaHours.fridayClose
                            ), CinemaOpeningHours(
                                cinemaHours.saturdayOpen, cinemaHours.saturdayClose
                            ), CinemaOpeningHours(
                                cinemaHours.sundayOpen, cinemaHours.sundayClose
                            )
                        )
                    )
                )
            }

            onFinished(Result.success(cinemasToGive))
        }
    }

    override fun insertRegistrationData(
        registrationData: RegistrationData, onFinished: () -> Unit
    ) {

        CoroutineScope(Dispatchers.IO).launch {


            if (registrationData.imageUris != null) {
                for (registrationPhoto in registrationData.imageUris) {
                    registoDao.insertPhotosRegistration(
                        PhotoDataRegistration(
                            0,
                            registrationData.uuid,
                            registrationPhoto.dataPhoto,
                            registrationPhoto.Timestamp
                        )
                    )
                }
            }

            val registo = RegistoData(
                registrationData.uuid,
                registrationData.filmeId,
                registrationData.cinemaId,
                registrationData.name,
                registrationData.cinema,
                registrationData.rate,
                registrationData.date,
                registrationData.observacoes,
                registrationData.postcode
            )
            registoDao.insert(registo)

            Log.i("APP", "Inserted $registo in DB")

            onFinished()

        }
    }

    override fun getRegistrationById(
        filmeId: String, onFinished: (Result<RegistrationData>) -> Unit
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            val registo = registoDao.getById(filmeId)

            val photos =
                registoDao.getPhotosByUuid(registo.uuid).map { Photo(it.dataPhoto, it.Timestamp) }

            val registoToGive = RegistrationData(
                registo.uuid,
                registo.filmeId,
                registo.cinemaId,
                registo.name,
                registo.cinema,
                registo.rate,
                registo.date,
                photos,
                registo.observacoes,
                registo.postCode
            )

            onFinished(Result.success(registoToGive))
        }
    }

    override fun getCinemaObjectByName(name: String, onFinished: (Result<Cinema>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            val cinema = cinemasDao.getCinemaByName(name)

            val cinemaHours = cinemasDao.getAllHoursFromCinema(cinema.id.toInt())

            val cinemaPhotos = cinemasDao.getPhotos(cinema.id.toInt()).map { it.photoUrl }

            val cinemaRatings =
                cinemasDao.getRatings(cinema.id.toInt()).map { CinemaRating(it.category, it.score) }

            val cinemaToGive = Cinema(
                cinema.id.toInt(),
                cinema.name,
                cinema.provider,
                cinema.logo,
                cinema.latitude,
                cinema.longitude,
                cinema.address,
                cinema.postcode,
                cinema.county,
                cinemaPhotos,
                cinemaRatings,
                CinemaDays(
                    CinemaOpeningHours(
                        cinemaHours.mondayOpen,
                        cinemaHours.mondayClose,
                    ), CinemaOpeningHours(
                        cinemaHours.tuesdayOpen, cinemaHours.tuesdayClose
                    ), CinemaOpeningHours(
                        cinemaHours.wednesdayOpen, cinemaHours.wednesdayClose
                    ), CinemaOpeningHours(
                        cinemaHours.thursdayOpen, cinemaHours.thursdayClose
                    ), CinemaOpeningHours(
                        cinemaHours.fridayOpen, cinemaHours.fridayClose
                    ), CinemaOpeningHours(
                        cinemaHours.saturdayOpen, cinemaHours.saturdayClose
                    ), CinemaOpeningHours(
                        cinemaHours.sundayOpen, cinemaHours.sundayClose
                    )
                )
            )

            onFinished(Result.success(cinemaToGive))
        }
    }

    override fun getCinemaObjectById(Id: Int, onFinished: (Result<Cinema>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            val cinema = cinemasDao.getCinemaById(Id)

            val cinemaHours = cinemasDao.getAllHoursFromCinema(cinema.id.toInt())

            val cinemaPhotos = cinemasDao.getPhotos(cinema.id.toInt()).map { it.photoUrl }

            val cinemaRatings =
                cinemasDao.getRatings(cinema.id.toInt()).map { CinemaRating(it.category, it.score) }

            val cinemaToGive = Cinema(
                cinema.id.toInt(),
                cinema.name,
                cinema.provider,
                cinema.logo,
                cinema.latitude,
                cinema.longitude,
                cinema.address,
                cinema.postcode,
                cinema.county,
                cinemaPhotos,
                cinemaRatings,
                CinemaDays(
                    CinemaOpeningHours(
                        cinemaHours.mondayOpen,
                        cinemaHours.mondayClose,
                    ), CinemaOpeningHours(
                        cinemaHours.tuesdayOpen, cinemaHours.tuesdayClose
                    ), CinemaOpeningHours(
                        cinemaHours.wednesdayOpen, cinemaHours.wednesdayClose
                    ), CinemaOpeningHours(
                        cinemaHours.thursdayOpen, cinemaHours.thursdayClose
                    ), CinemaOpeningHours(
                        cinemaHours.fridayOpen, cinemaHours.fridayClose
                    ), CinemaOpeningHours(
                        cinemaHours.saturdayOpen, cinemaHours.saturdayClose
                    ), CinemaOpeningHours(
                        cinemaHours.sundayOpen, cinemaHours.sundayClose
                    )
                )
            )

            onFinished(Result.success(cinemaToGive))
        }
    }

    override fun getAllRegistrationData(onFinished: (Result<List<RegistrationData>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {


            val registos = registoDao.getAll().map {
                RegistrationData(
                    it.uuid,
                    it.filmeId,
                    it.cinemaId,
                    it.name,
                    it.cinema,
                    it.rate,
                    it.date,
                    registoDao.getPhotosByUuid(it.uuid).map { Photo(it.dataPhoto, it.Timestamp) },
                    it.observacoes,
                    it.postCode
                )
            }

            onFinished(Result.success(registos))
        }
    }

    override fun deleteAllReg() {

        CoroutineScope(Dispatchers.IO).launch {

            registoDao.deleteAll()

            //filmesDao.deleteAll()
        }
    }
}