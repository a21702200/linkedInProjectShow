package pt.ulusofona.deisi.cm2223.g21702200.data.remote

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g21702200.models.Cinema
import pt.ulusofona.deisi.cm2223.g21702200.models.Filmes
import pt.ulusofona.deisi.cm2223.g21702200.models.FilmesModel
import pt.ulusofona.deisi.cm2223.g21702200.models.ProjectData
import pt.ulusofona.deisi.cm2223.g21702200.models.RegistrationData
import java.io.IOException

class ProjectOkHttp(
    private val baseUrl: String, private val apiKey: String, private val client: OkHttpClient
) : ProjectData() {

    override fun insertFilmes(filmes: Filmes, onFinished: () -> Unit) {
        Log.e("APP", "web service is not able to insert filmes")
    }

    override fun getAllCinemas(onFinished: (Result<List<Cinema>>) -> Unit) {
        Log.e("APP", "web service is not able get id from cinema")
    }

    override fun insertCinemas(cinemas: List<Cinema>) {
        Log.e("APP", "web service is not able to clear all cinemas")
    }

    override fun insertRegistrationData(
        registrationData: RegistrationData, onFinished: () -> Unit
    ) {
        Log.e("APP", "web service is not able to clear all registos")
    }

    override fun getAllRegistrationData(onFinished: (Result<List<RegistrationData>>) -> Unit) {
        Log.e("APP", "web service is not able to get all registos")
    }

    override fun getRegistrationById(
        filmeId: String, onFinished: (Result<RegistrationData>) -> Unit
    ) {
        Log.e("APP", "web service is not able to get all registos by id")
    }

    override fun getCinemaObjectByName(name: String, onFinished: (Result<Cinema>) -> Unit) {
        Log.e("APP", "web service is not able to get getCinemaObject")
    }


    override fun getFilmeById(id: String, onFinished: (Result<Filmes>) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            val request: Request = Request.Builder().url("$baseUrl/?apikey=$apiKey&i=$id").build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onFinished(Result.failure(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        onFinished(Result.failure(IOException("Unexpected code $response")))
                    } else {
                        val body = response.body?.string()
                        if (body != null) {
                            val jsonObjectFilme = JSONObject(body)

                            val filme = Filmes(
                                jsonObjectFilme.getString("imdbID"),
                                jsonObjectFilme.getString("Title"),
                                jsonObjectFilme.getString("Poster"),
                                jsonObjectFilme.getString("Genre"),
                                jsonObjectFilme.getString("Plot"),
                                jsonObjectFilme.getString("Released"),
                                jsonObjectFilme.getString("imdbRating"),
                                jsonObjectFilme.getString("Language"),
                                jsonObjectFilme.getString("Awards"),
                                jsonObjectFilme.getString("Actors"),
                                jsonObjectFilme.getString("Director"),
                                jsonObjectFilme.getString("imdbVotes")

                            )
                            onFinished(Result.success(filme))
                        }
                    }
                }
            })
        }
    }

    override fun getFilmesByName(
        searchName: String, onFinished: (Result<List<FilmesModel>>) -> Unit
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            //println("vim recolher movies")

            val request: Request =
                Request.Builder().url("$baseUrl/?apikey=$apiKey&s=$searchName").build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    //println("Deu exceção")

                    onFinished(Result.failure(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        onFinished(Result.failure(IOException("Unexpected code $response")))
                    } else {
                        val body = response.body?.string()
                        if (body != null) {

                            val jsonObjectFilmes = JSONObject(body)

                            if (jsonObjectFilmes.has("Search")) {

                                val jsonFilmesModelList = jsonObjectFilmes.getJSONArray("Search")
                                val appFilmesModel = mutableListOf<FilmesModel>()

                                for (filme in 0 until jsonFilmesModelList.length()) {
                                    val jsonFilme = jsonFilmesModelList.getJSONObject(filme)

                                    appFilmesModel.add(
                                        FilmesModel(
                                            jsonFilme.getString("Title"),
                                            jsonFilme.getString("imdbID"),
                                        )
                                    )
                                }

                                onFinished(Result.success(appFilmesModel))
                            } else {

                                //println("Nada encontrado")

                                onFinished(Result.failure(IOException("No 'Search' key found in the JSON response")))

                            }
                        }
                    }
                }
            })
        }
    }

    override fun getAllFilmes(onFinished: (Result<List<Filmes>>) -> Unit) {
        Log.e("APP", "web service is not able to get getAllFilmes")
    }

    override fun deleteAllReg() {
        Log.e("APP", "web service is not able to get deleteAllReg")
    }

    override fun getCinemaObjectById(Id: Int, onFinished: (Result<Cinema>) -> Unit) {
        Log.e("APP", "web service is not able to get getCinemaObjectById")
    }
}