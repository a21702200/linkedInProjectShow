**André Cruz** <br>
(Abrir ficheiro readme sozinho para melhor visibilidade)

**(Este trabalho foi um projeto de faculdade, onde exigiu o cumprimento de requesitos obrigatorios e suas restrições(este ultimo, por exemplo, sem implementar qualquer tipo de "LiveData" ou "RxJava"))**

**(Existiu um maior desenvolvimento após avaliação deste trabalho, o qual poderá ter documentação desatualizada, contudo todo o codigo neste github está atualizado e na mais recente versão)**

**(Neste repositorio não inclui persistencia de dados na rotação do ecra(lista) com o uso do "Parcelable"(implementado na class "RegistrationData"), no entanto no meu repositorio privado essa "funcionalidade" encontra-se implementada)**

## Secreenshots App

<div class="image-container">
  <img src="dashboard.png" width="300">
  <img src="Registo(picker2).png" width="300">
  <img src="OnlinePicker.png" width="300">
</div>

<div class="image-container">
  <img src="offlinePicker.png" width="300">
  <img src="Registo.png" width="300">
  <img src="Registo2.png" width="300">
</div>

<div class="image-container"> 
  <img src="Registo(cinemas).png" width="300">
  <img src="list.png" width="300">
  <img src="listLandscape.png" width="300">
</div>

<div class="image-container">
  <img src="FilterList.png" width="300">
  <img src="filterLandscape.png" width="300">
  <img src="Imdb_movie.png" width="300">
</div>


<div class="image-container">
  <img src="DetailMovie.png" width="300">
  <img src="DetailMovie1.png" width="300">
  <img src="DetailMovie2.png" width="300">
</div>

<div class="image-container">
  <img src="Mapa.png" width="300">
  <img src="cinemas.png " width="300">
  <img src="cinemasInfo.png" width="300">
</div>

<div class="image-container">
  <img src="layoutMain.png" width="300">
  <img src="voice.png " width="300">
</div>

<br>

## Screenshots Multi Idioma

### Espanhol (**layout/versão antiga**)

<div class="image-container">
  <img src="Dashboard_es.png" width="300">
  <img src="cinemaInfoEs.png " width="300">
  <img src="DetailMovie_es.png" width="300">
</div>

### Português (**layout/versão antiga**)

<div class="image-container">
  <img src="Dasboard_pt.png" width="300">
  <img src="cinemaInfoPt.png " width="300">
  <img src="DetailMovie_pt.png" width="300">
</div>


## Dados introduzidos hardcoded na aplicação
No DashBoard a imagem de apresentação com o imagem do filme "Black Panther", com titulo do mesmo e "Amazing Movie"(este ultimo muda consoante o idioma).

## Funcionalidades Implementadas

- Dashboard<br>
- Apresentação dos filmes - Lista<br>
- Apresentação dos filmes - Lista - Rotação<br>
- Detalhe do filme (sem fotografias)<br>
- Registo de filmes (sem fotografias)<br>
- Suporte multi-idioma<br>
- Navegabilidade<br><br>

- Registo de Filmes (Pré seleciona o cinema(spinner) consoante geolocalização)<br>
- Inserir corretamente na base de dados <br>
- Validação e obtenção dos dados do filme via API<br>
- Validação e obtenção dos dados do cinema via JSON<br>
- Utilização de geo-localização (além da utilização no ecrã lista e mapa, pré seleciona o cinema(spinner) mais próximo no momento/ecrã do registo) <br>
- Inserir fotografias na base de dados
- Apresentação dos filmes - Lista<br>
- Apresentação dos filmes - Mapa<br>
- Detalhe do filme (sem fotografias)<br>
- Detalhe do filme (apenas a parte das fotografias)
- Pesquisa de filmes por voz - Funcionalidade Avançada
- Funcionamento Offline - Funcionalidade Avançada<br>
- Video (versão antiga, demonstrativo apenas)<br>
- Ficheiro chatgpt.txt<br><br>

- Regista Já (virar devagar o telemovel no simulador, se virar com os botoes do simulador, deteta shaking devido à rapidez do mesmo e vai para o ecra de registo)<br>
- Filtros - Apresentação em Lista<br>
- Ordenação - Apresentação em Lista<br>

**No Dashboard foram adicionados pequenos algoritmos que calcula:**<br>
-cinema mais frequentado<br>
-Numero total de registos<br>
-Media de rates<br>

[Video](https://www.youtube.com/watch?v=ahoMPmi38v0) -> **VERSÃO ANTIGA DO PROJETO, APENAS DEMONSTRATIVO**

## Descrição nome classes, métodos e atributos

- Cinemas: (Usado para obter todas as informações de cada cinema providenciado do json, tais como: id, nome, provedor de cinema, logo, latitude e longitude, morada, codigo postal, cidade, fotos, ratings e Horas de funcionamento)

data class Cinema(<br>
val cinema_id: Int,<br>
val cinema_name: String,<br>
val cinema_provider: String,<br>
val logo_url: String?,<br>
val latitude: Double,<br>
val longitude: Double,<br>
val address: String,<br>
val postcode: String,<br>
val county: String,<br>
val photos: List<String>?,<br>
val ratings: List<CinemaRating>,<br>
val opening_hours: CinemaDays<br>
)<br>

data class CinemaRating(<br>
val category: String,<br>
val score: Int<br>
)<br>

data class CinemaDays(<br>
val Monday: CinemaOpeningHours,<br>
val Tuesday: CinemaOpeningHours,<br>
val Wednesday: CinemaOpeningHours,<br>
val Thursday: CinemaOpeningHours,<br>
val Friday: CinemaOpeningHours,<br>
val Saturday: CinemaOpeningHours,<br>
val Sunday: CinemaOpeningHours<br>
)<br>

data class CinemaOpeningHours(<br>
val open: Timestamp,<br>
val close: Timestamp<br>
)<br>

- Filmes: (Usado para obter as informações vindas da API de um filme escolhido que o utilizador irá registar ou visualizar detalhes, tais como: id, nome do filme, url cartaz, genero, sinopse, lancamento, avaliacao e link)

data class Filmes (<br>
val id: String,<br>
val filme: String, // nome<br>
val cartaz: String,<br>
val genero: String,<br>
val sinopse: String,<br>
val lancamento: String,<br>
val avaliacao: String,<br>
val languages: String,<br>
val awards: String,<br>
val actors: String,<br>
val director: String,<br>
val imdbVotes: String<br>
)<br>

- FilmesModel: (Usado para obter os filmes em pré seleção para o registo à posteriori, inseridos na recycler view de pesquisa por nome, onde guarda, os titulos encontrados por aquele nome e imdb id de cada um)

data class FilmesModel (
val Title: String,
val imdbID: String
)<br>

- Registrationdata: (Usado para registar o filme na obtenção do id e nome do filme vindo da api, como tambem recolher as informações do utilizador como o cinema(id e nome) onde visualizou, rate, data quando visualizou e observacoes)

data class RegistrationData(<br>
val id: Int,<br>
val filmeId: String,<br>
val cinemaId: Int,<br>
val name: String,<br>
val cinema: String,<br>
val rate: String,<br>
val date: String,<br>
val imageUri: String? = null,<br>
val observacoes: String<br>
val postcode: String<br>
)<br>

- Photo: (Usado para guardar as fotografias inseridas no momento do registo na db e na apresentação detalhes do registo)

data class Photo(<br>
val dataPhoto: String,<br>
val Timestamp: Long<br>
)<br>


A classe ProjectData é usada para implementar os metodos necessarios no ProjectRoom, ProjectRepository e ProjectOkHttp.<br>

Project Data    
abstract class ProjectData {

    //Cinemas
    abstract fun getAllCinemas(onFinished: (Result<List<Cinema>>) -> Unit)

    abstract fun insertCinema (cinemas: List<Cinema>, onFinished: () -> Unit)

    abstract fun getCinemaObjectByName(name: String, onFinished: (Result<Cinema>) -> Unit)

    abstract fun getCinemaObjectById(Id: Int, onFinished: (Result<Cinema>) -> Unit)

    //Registo
    abstract fun insertRegistrationData (registrationData: RegistrationData, onFinished: () -> Unit)

    abstract fun getAllRegistrationData(onFinished: (Result<List<RegistrationData>>) -> Unit)

    abstract fun getRegistrationById(filmeId: String, onFinished: (Result<RegistrationData>) -> Unit)

    //Filmes
    abstract fun insertFilmes (filmes: Filmes, onFinished: () -> Unit)

    abstract fun getFilmeById (id:String, onFinished: (Result<Filmes>) -> Unit)

    abstract fun getFilmesByName(searchName: String, onFinished: (Result<List<FilmesModel>>) -> Unit)

    //Filmes to pick
    abstract fun insertFilmesAntesRegisto(filmes: List<FilmesModel>, onFinished: () -> Unit)

}<br>

## Métodos usados neste ProjectData(que estão presentes ProjectOkHttp, ProjectRepository e ProjectRoom)

getAllCinemas -> obter todos os cinemas<br>
getCinemaObjectByName -> obter objeto cinema em especifico pelo nome<br>
getCinemaObjectById -> obter objeto cinema em especifico pelo id<br>
insertCinema -> inserir cinemas na base de dados vindo do json<br>

insertRegistrationData -> inserir registo do utilizador na base de dados<br>
getAllRegistrationData -> obter todos os registos como objectos<br>
getRegistrationById -> obter o registo pelo uuid na bd<br>

insertFilmes -> inserir detalhes de filmes vindos da api na base de dados<br>
getFilmeById -> obter detalhes do filme por id (id vindo da api)<br>
getFilmesByName -> obter detalhes dos filmes pesquisados pelo nome (detalhes escolhidos vindos da api) <br>

insertFilmesAntesRegisto -> inserir filmes encontrados à priori na base de dados encontrados pelo o nome na api, para o utilizador depois escolher o filme pretendido a registar<br>

## Fontes Usadas

ChatGPT Traduções(xml traduzido):<br>

ChatGPT -> Tradução em Espanhol (Apenas foi usado traduções chatgpt para espanhol) (DESATUALIZADO)
![GhatGPT Espanhol](chatgpt_es.png "ChatGPT Espanhol")

- Foi usado algumas fontes de informação tais como StackOverflow, ChatGPT e geeksforgeeks<br>
- Foram concedidos alguns repositorios github pelos professores da cadeira com exemplos na ajuda ao desenvolvimento deste projeto aos quais não irei citar, devido ao repositorio estar publico<br>

Fontes usadas para icones bottom navigation:<br>
https://fonts.google.com/

"Usado" para saber como fazer abertura e leitura do ficheiro json: <br>
https://stackoverflow.com/questions/56962608/how-to-read-json-file-from-assests-in-android-using-kotlin

Usado saber como converter timestamp para long, em relação à db(visto que a db só aceita tipos de dados básicos, como um Long): <br>
https://stackoverflow.com/questions/73133398/how-to-get-sorted-data-from-room-db-on-the-base-of-date-timestamp

Usado para entender como fazer parse do ficheiro json(cinemas.json) e do que vem dos pedidos Api com o uso de jsonArray e jsonObject: <br>
https://www.tutorialspoint.com/how-to-parse-json-objects-on-android-using-kotlin

-> Inicialmente era utilizado glide, mas foi mudado para o Picasso por crashar a app (apresentação de imagens com link) https://github.com/square/picasso<br>
-> implementação dos markers (Mapa)<br>

## Notas
->Por vezes os logs na consola de distancia estão a reportar de modo confuso a distancia, mas é a correta e funcina como devido.
Foi testado inumeras vezes, >500 metros, entre 500 e 1000 metros, como tambem >1000(que já nao aparece nenhum resultado), mesmo com auxilio do google maps e a ferramenta de medições de distancia, afetou sempre a lista corretamente com os filmes do cinema entre as distancia pretendida, tendo em conta a localização atual

-> Por vezes necessário coldBoot caso não funcione o gps (Fused Location)

-> Foi detetado que mesmo após a cedencia de permissioes de geolocalização pela primeira vez, para que fusedLocation e respetivos listeners sejam registados em cada ecra funcionem, é necessario reiniciar a app (é visivel no android status bar)

-> No "Filtrar" da lista, na combinação com nome ou não, só é aplicado uma vez a proximidade(poderá fazer um filtro novamente caso queira), primeiro é feito um filtro por nome e depois por localização. Essa "atualização" quando é escolhida a opção localização, fica dependente pelo TIME_BETWEEN_UPDATES do próprio fusedlocation

-> Foi detetado que as longitudes de latitudes de alguns cinemas, segundo o ficheiro cinemas.json, não são as reais (ficheiro concedido pelo professor da cadeira)

-> No funcionamento offline, quando é apresentado o ecrã para selecionar filmes(pickMovieFragment), apenas aparecem os filmes que já foram retiradas todas as informações da API, i.e. os filmes que já foram registados

-> Foi garantido que o ecrã só roda apenas no ListFragment e FilterFragment(visto que está associado à operação do ecra lista e "necessita" de ficar horizontal para ser coerente com a rotação em que estava à priori(lista))
