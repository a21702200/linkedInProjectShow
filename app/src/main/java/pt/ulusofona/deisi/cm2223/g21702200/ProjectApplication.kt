package pt.ulusofona.deisi.cm2223.g21702200

import android.app.Application
import android.util.Log
import okhttp3.OkHttpClient
import pt.ulusofona.deisi.cm2223.g21702200.data.ProjectRepository
import pt.ulusofona.deisi.cm2223.g21702200.data.local.DatabaseApp
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom
import pt.ulusofona.deisi.cm2223.g21702200.data.remote.ProjectOkHttp

class ProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ProjectRepository.init(initLOTRRoom(), initLOTROkHttp(), this)

        Log.i("APP", "Initialized repository")

        FusedLocation.start(this)
    }

    private fun initLOTROkHttp(): ProjectOkHttp {
        return ProjectOkHttp(
            APP_API_BASE_URL, APP_API_TOKEN, OkHttpClient()
        )
    }


    private fun initLOTRRoom(): ProjectRoom {
        return ProjectRoom(
            DatabaseApp.getInstance(applicationContext).cinemasDao(),
            DatabaseApp.getInstance(applicationContext).filmesDao(),
            DatabaseApp.getInstance(applicationContext).registoDao()
        )
    }
}