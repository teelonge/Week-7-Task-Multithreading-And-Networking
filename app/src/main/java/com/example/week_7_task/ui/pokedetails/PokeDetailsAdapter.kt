package com.example.week_7_task.ui.pokedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.week_7_task.R
import com.example.week_7_task.databinding.ItemPokemonBinding
import com.example.week_7_task.databinding.PokemonImageBinding

/**
 * Adapter class for getting each sprite image for each pokemon
 * Takes in a list of string which contains each pokemon sprite url and converts it to an image
 */
class PokeDetailsAdapter(private val pokemonSprites: List<String>) :
    RecyclerView.Adapter<PokeDetailsAdapter.DetailsViewHolder>() {


    class DetailsViewHolder(private val binding: PokemonImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemonSprites: String) {
            //Converts the url to uri and loads the images with glide
            val imageUri = pokemonSprites?.toUri()?.buildUpon()?.scheme("https")?.build()
            Glide.with(binding.root.context)
                .load(imageUri).apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_status_animation)
                        .error(R.drawable.ic_error_image)
                )
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val binding = PokemonImageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(pokemonSprites[position])
    }

    override fun getItemCount(): Int {
        return pokemonSprites.size
    }

}