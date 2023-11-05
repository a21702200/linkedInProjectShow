package pt.ulusofona.deisi.cm2223.g21702200.data

import android.content.Context
import android.util.Log
import pt.ulusofona.deisi.cm2223.g21702200.Connectivity
import pt.ulusofona.deisi.cm2223.g21702200.models.Cinema
import pt.ulusofona.deisi.cm2223.g21702200.models.Filmes
import pt.ulusofona.deisi.cm2223.g21702200.models.FilmesModel
import pt.ulusofona.deisi.cm2223.g21702200.models.ProjectData
import pt.ulusofona.deisi.cm2223.g21702200.models.RegistrationData
import java.io.IOException

class ProjectRepository(
    private val context: Context, private val local: ProjectData, private val remote: ProjectData
) : ProjectData() {

    override fun getAllCinemas(onFinished: (Result<List<Cinema>>) -> Unit) {
        Log.e("APP", "Repository method can’t getCinema")
    }

    override fun getAllRegistrationData(onFinished: (Result<List<RegistrationData>>) -> Unit) {
        Log.e("APP", "Repository method can’t getRegistrationDataNormal")
    }

    override fun getRegistrationById(
        filmeId: String, onFinished: (Result<RegistrationData>) -> Unit
    ) {
        Log.e("APP", "Repository method can’t getRegistrationById")
    }

    override fun getCinemaObjectByName(name: String, onFinished: (Result<Cinema>) -> Unit) {
        Log.e("APP", "Repository method can’t getCinemaObject")
    }

    override fun getFilmeById(id: String, onFinished: (Result<Filmes>) -> Unit) {
        if (Connectivity.isOnline(context)) {
            remote.getFilmeById(id) { result ->
                if (result.isSuccess) {
                    refreshHistory(result.getOrNull()!!) {
                        local.getFilmeById(id) { result ->
                            onFinished(result)
                        }
                    }
                }
            }
        } else {
            local.getFilmeById(id) { result ->
                onFinished(result)
            }
        }
    }

    private fun refreshHistory(filme: Filmes, onFinished: () -> Unit) {
        local.insertFilmes(filme) {
            onFinished()
        }
    }


    override fun getFilmesByName(
        searchName: String, onFinished: (Result<List<FilmesModel>>) -> Unit
    ) {

        if (Connectivity.isOnline(context)) {
            remote.getFilmesByName(searchName) { result ->

                //println("resultado failure or not " + result)

                if (result.isSuccess) {

                    onFinished(result)

                } else {
                    onFinished(Result.failure(IOException("No 'Search' key found in the JSON response")))
                }
            }
        } else {

            val filmesToGive = mutableListOf<FilmesModel>()

            local.getAllFilmes { resultFilmes ->

                if (resultFilmes.isSuccess) {

                    val filmesStored = resultFilmes.getOrNull()!!

                    if (filmesStored.isNotEmpty()) {
                        for (filmeStored in filmesStored) {

                            if (filmeStored.filme.lowercase().contains(searchName.lowercase())) {
                                filmesToGive.add(FilmesModel(filmeStored.filme, filmeStored.id))
                            }
                        }

                        if (filmesToGive.isNotEmpty()) {
                            onFinished(
                                Result.success(
                                    filmesToGive
                                )
                            )
                        } else {
                            onFinished(Result.failure(IOException("Dont find any movie on db")))
                        }
                    } else {
                        onFinished(Result.failure(IOException("Dont find any movie on db")))
                    }
                } else {
                    onFinished(Result.failure(IOException("Dont find any movie on db")))
                }
            }
        }
    }

    override fun getAllFilmes(onFinished: (Result<List<Filmes>>) -> Unit) {
        Log.e("APP", "Repository method can’t getAllFilmes")
    }

    override fun deleteAllReg() {
        Log.e("APP", "Repository method can’t deleteAllReg")
    }


    override fun getCinemaObjectById(Id: Int, onFinished: (Result<Cinema>) -> Unit) {
        Log.e("APP", "Repository method can’t getCinemaObjectById")
    }


    override fun insertCinemas(cinemas: List<Cinema>) {
        Log.e("APP", "Repository method can’t insertCinema")
    }

    override fun insertFilmes(filmes: Filmes, onFinished: () -> Unit) {
        Log.e("APP", "Repository method can’t insertFilmes")
    }

    override fun insertRegistrationData(
        registrationData: RegistrationData, onFinished: () -> Unit
    ) {
        Log.e("APP", "Repository method can’t insertRegistrationData")
    }

    companion object {
        private var instance: ProjectRepository? = null

        fun init(local: ProjectData, remote: ProjectData, context: Context) {
            if (instance == null) {
                instance = ProjectRepository(context, local, remote)
            }
        }

        fun getInstance(): ProjectRepository {
            if (instance == null) {
                // Primeiro temos de invocar o init, se não lança esta Exception
                throw IllegalStateException("singleton not initialized")
            }
            return instance as ProjectRepository
        }
    }
}