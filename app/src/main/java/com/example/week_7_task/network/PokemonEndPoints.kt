package com.example.week_7_task.network

import com.example.week_7_task.PokemonDetails
import com.example.week_7_task.Pokemons
import com.example.week_7_task.Result
import com.example.week_7_task.Type
import com.example.week_7_task.ui.pokelive.PokeliveViewModel
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonEndPoints {

    // Calls the pokeapi to return the first 20 pokemons
    @GET("pokemon?limit=20&offset=0")
    fun getPokemonAsync():
            Deferred<Pokemons>

    // Returns each pokemon with all their corresponding details
    @GET
    fun getParticularPokemonDetailsAsync(@Url url : String) : Deferred<PokemonDetails>

    // Returns the next 30 or previous 30 if exists of the pokemon
    @GET
    fun getPreviousPokemonsAsync(@Url url : String) : Deferred<Pokemons>



}

