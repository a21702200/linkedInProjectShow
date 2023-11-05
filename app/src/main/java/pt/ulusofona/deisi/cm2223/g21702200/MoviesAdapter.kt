package pt.ulusofona.deisi.cm2223.g21702200


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectcm.databinding.ItemMovieListBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21702200.data.local.ProjectRoom
import pt.ulusofona.deisi.cm2223.g21702200.models.RegistrationData


class MoviesAdapter(
    private val onItemClick: (String) -> Unit,
    //private val context: Context,
    private var room: ProjectRoom,
    private var items: List<RegistrationData> = listOf(),
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(val binding: ItemMovieListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        // holder.itemView.setOnClickListener {

        //println("item name first letter -> " + items[position].name[0])

        //if (items[position].name[0].toString().equals("a") || items[position].name[0].toString().equals("A")){
        //  Toast.makeText(context, "Cinema name -> " + items[position].cinema, Toast.LENGTH_SHORT).show()
        //}else{
        //  onItemClick(items[position].id)
        // }
        //}


        //val color = Color.parseColor("#80a0cc")

        //val drawableColor = ColorDrawable(color)

        //holder.itemView.background = drawableColor

        holder.itemView.setOnClickListener { onItemClick(items[position].uuid) }


        room.getFilmeById(items[position].filmeId) { result ->


            val cartaz = result.getOrNull()!!.cartaz

            //println("cartaz -> " + cartaz)
            CoroutineScope(Dispatchers.Main).launch {
                holder.binding.movieName.text = items[position].name
                holder.binding.rate.text = "Rate: " + items[position].rate
                holder.binding.date.text = formatDate.format(items[position].date)
                holder.binding.cinemaName?.text = items[position].cinema



                Picasso.get().load(cartaz).into(holder.binding.imageView)
            }
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<RegistrationData>) {
        this.items = items
        notifyDataSetChanged()
    }
}

