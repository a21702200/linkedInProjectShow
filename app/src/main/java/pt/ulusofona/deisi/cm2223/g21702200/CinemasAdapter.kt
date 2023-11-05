package pt.ulusofona.deisi.cm2223.g21702200


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectcm.databinding.ItemCinemaOrMovieBinding
import pt.ulusofona.deisi.cm2223.g21702200.models.Cinema

class CinemasAdapter(
    private val onItemClick: (Int) -> Unit, private var items: List<Cinema> = listOf()
) : RecyclerView.Adapter<CinemasAdapter.CinemaViewHolder>() {

    class CinemaViewHolder(val binding: ItemCinemaOrMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaViewHolder {
        return CinemaViewHolder(
            ItemCinemaOrMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onItemClick(items[position].cinema_id) }

        holder.binding.titleTextView.text = items[position].cinema_name
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<Cinema>) {
        this.items = items
        notifyDataSetChanged()
    }
}
