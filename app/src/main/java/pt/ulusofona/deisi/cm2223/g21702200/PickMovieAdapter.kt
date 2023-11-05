package pt.ulusofona.deisi.cm2223.g21702200


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectcm.databinding.ItemCinemaOrMovieBinding
import pt.ulusofona.deisi.cm2223.g21702200.models.FilmesModel

class PickMovieAdapter(
    private val onItemClick: (String, String) -> Unit,
    private var items: List<FilmesModel> = listOf()
) : RecyclerView.Adapter<PickMovieAdapter.MovieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemCinemaOrMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClick(
                items[position].Title, items[position].imdbID
            )
        }


        holder.binding.titleTextView.text = items[position].Title
    }


    class MovieViewHolder(val binding: ItemCinemaOrMovieBinding) :
        RecyclerView.ViewHolder(binding.root)


    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<FilmesModel>) {
        this.items = items
        notifyDataSetChanged()
    }
}