package pt.ulusofona.deisi.cm2223.g21702200

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectcm.databinding.ItemPhotoRegistrationBinding
import pt.ulusofona.deisi.cm2223.g21702200.algorithms.decodeBase64ToBitmap
import pt.ulusofona.deisi.cm2223.g21702200.models.Photo

class PhotoRegistrationAdapter(private var items: List<Photo> = listOf()) :
    RecyclerView.Adapter<PhotoRegistrationAdapter.PhotoRegistrationViewHolder>() {

    class PhotoRegistrationViewHolder(val binding: ItemPhotoRegistrationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoRegistrationViewHolder {
        return PhotoRegistrationViewHolder(
            ItemPhotoRegistrationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: PhotoRegistrationViewHolder, position: Int) {

        val bitmapDecoded = decodeBase64ToBitmap(items[position].dataPhoto)

        val photo = holder.binding.photoRegistration

        photo.setImageBitmap(bitmapDecoded)

    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<Photo>) {
        this.items = items
        notifyDataSetChanged()
    }
}


