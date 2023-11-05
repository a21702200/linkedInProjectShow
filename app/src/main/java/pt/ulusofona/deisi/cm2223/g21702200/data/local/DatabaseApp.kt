package pt.ulusofona.deisi.cm2223.g21702200.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CinemasData::class, FilmeData::class, RegistoData::class, Rating::class, OpeningHours::class, Photos::class, PhotoDataRegistration::class],
    version = 113
)

abstract class DatabaseApp : RoomDatabase() {

    abstract fun cinemasDao(): CinemasDao
    abstract fun filmesDao(): FilmesDao
    abstract fun registoDao(): RegistoDao

    companion object {
        private var instance: DatabaseApp? = null

        fun getInstance(context: Context): DatabaseApp {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context, DatabaseApp::class.java, "app_db"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance as DatabaseApp
            }
        }
    }
}