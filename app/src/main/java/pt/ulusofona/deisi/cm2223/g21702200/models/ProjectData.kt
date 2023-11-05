package pt.ulusofona.deisi.cm2223.g21702200.models


abstract class ProjectData {

    //Cinemas
    abstract fun getAllCinemas(onFinished: (Result<List<Cinema>>) -> Unit)

    abstract fun insertCinemas(cinemas: List<Cinema>)

    abstract fun getCinemaObjectByName(name: String, onFinished: (Result<Cinema>) -> Unit)

    abstract fun getCinemaObjectById(Id: Int, onFinished: (Result<Cinema>) -> Unit)

    //Registo
    abstract fun insertRegistrationData(registrationData: RegistrationData, onFinished: () -> Unit)

    abstract fun getAllRegistrationData(onFinished: (Result<List<RegistrationData>>) -> Unit)

    abstract fun getRegistrationById(
        filmeId: String, onFinished: (Result<RegistrationData>) -> Unit
    )

    //Filmes
    abstract fun insertFilmes(filmes: Filmes, onFinished: () -> Unit)

    abstract fun getFilmeById(id: String, onFinished: (Result<Filmes>) -> Unit)

    abstract fun getFilmesByName(
        searchName: String, onFinished: (Result<List<FilmesModel>>) -> Unit
    )

    abstract fun getAllFilmes(onFinished: (Result<List<Filmes>>) -> Unit)

    abstract fun deleteAllReg()

}