package com.example.week_7_task.ui.pokelive

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.week_7_task.*
import com.example.week_7_task.databinding.ItemPokemonBinding

/**
 * Adapter for binding the views of each pokemon received from the API in the all pokemon fragment
 */
class PokeliveAdapter(private val resultingPokemons: List<PokemonDetails>) : RecyclerView.Adapter<PokeliveAdapter.ViewHolder>() {

    var listener: RecyclerViewClickListener? = null

    class ViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // binds the various fields in the Item pokemon and sets the background based on the type of pokemon
        var pokeView = binding.pokeView
        fun bind(resultingPokemon: PokemonDetails) {
            binding.txtPokemonName.text = resultingPokemon.name
            binding.pokemonID.text = resultingPokemon.id.toString()
            binding.txtType1.text = resultingPokemon.types[0].type.name
            if (resultingPokemon.types.size > 1) {
                binding.txtType2.text = resultingPokemon.types[1].type.name
            }
            binding.layoutView.getPokemonColor(binding.root, resultingPokemon.types)

            // Gets the imageUrl of each pokemon and converts it to an ImageUri and then a imageView
            val imageUrl = resultingPokemon.sprites.front_default
            val imageUri = imageUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
            Glide.with(binding.root.context)
                .load(imageUri).apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_status_animation)
                        .error(R.drawable.ic_error_image)
                )
                .into(binding.imgPokemonImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPokemonBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultingPokemons[position])
        holder.pokeView.setOnClickListener {
            listener?.onRecyclerViewItemClicked(it, resultingPokemons[position])
        }

    }

    override fun getItemCount(): Int {
        return resultingPokemons.size
    }


}