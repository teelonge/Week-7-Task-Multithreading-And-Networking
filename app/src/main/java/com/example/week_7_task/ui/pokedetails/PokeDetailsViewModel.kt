package com.example.week_7_task.ui.pokedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.week_7_task.BASE_URL
import com.example.week_7_task.PokemonDetails
import com.example.week_7_task.network.PokemonEndPoints
import com.example.week_7_task.network.ServiceBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A viewmodel class that handles the logic of getting the pokemon details from
 * the API, it retrieves the pokemon and gets all the details places it in a livedata for
 * another fragment to access and use
 */
class PokeDetailsViewModel : ViewModel() {

    private val _resultingPokemon = MutableLiveData<PokemonDetails>()
    val resultingPokemon: LiveData<PokemonDetails>
        get() = _resultingPokemon

    private val _pokemonSprites = MutableLiveData<List<String>>()
    val pokemonSprites: LiveData<List<String>>
        get() = _pokemonSprites

    var id = 0
    var url = ""

    // Creates a job which is then used to create a coroutineScope
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Makes the call to the Endpoint to retrieve a particular pokemon based off the received url and id
    fun initNetworkRequest() {
        Log.d("POKEDETAILS", "Okay: $id}")
        Log.d("POKEDETAILS", "Okay: $url")
        coroutineScope.launch {
            val request = ServiceBuilder.buildService(PokemonEndPoints::class.java)
            val getResultingPokemanDeferred = request.getParticularPokemonDetailsAsync(url)
            try {
                val resultingPokemonDetail = getResultingPokemanDeferred.await()
                _resultingPokemon.postValue(resultingPokemonDetail)

                /* Gets all the pokemon sprites and saves them in array which in turn is saved
                in a liveData accessed outside the viewModel
                 */
                val pokemonSprites = Array(8) { "" }
                pokemonSprites[0] = resultingPokemonDetail.sprites.front_default.toString()
                pokemonSprites[1] = resultingPokemonDetail.sprites.back_default.toString()
                pokemonSprites[2] = resultingPokemonDetail.sprites.back_female.toString()
                pokemonSprites[3] = resultingPokemonDetail.sprites.back_shiny.toString()
                pokemonSprites[4] = resultingPokemonDetail.sprites.back_shiny_female.toString()
                pokemonSprites[5] = resultingPokemonDetail.sprites.front_female.toString()
                pokemonSprites[6] = resultingPokemonDetail.sprites.front_shiny.toString()
                pokemonSprites[7] = resultingPokemonDetail.sprites.front_shiny_female.toString()

                _pokemonSprites.value = pokemonSprites.toList()

            } catch (e: Exception) {

            }

        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}