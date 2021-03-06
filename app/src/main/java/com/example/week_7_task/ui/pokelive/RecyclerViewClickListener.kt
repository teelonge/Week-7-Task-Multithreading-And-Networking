package com.example.week_7_task.ui.pokelive

import android.view.View
import com.example.week_7_task.PokemonDetails

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view : View, pokemonDetails: PokemonDetails)
}