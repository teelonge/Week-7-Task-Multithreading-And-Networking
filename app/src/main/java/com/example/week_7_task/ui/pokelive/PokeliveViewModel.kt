package com.example.week_7_task.ui.pokelive

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week_7_task.PokemonDetails
import com.example.week_7_task.Pokemons
import com.example.week_7_task.Result
import com.example.week_7_task.UploadResponse
import com.example.week_7_task.network.NetworkClient

import com.example.week_7_task.network.PokemonEndPoints
import com.example.week_7_task.network.ServiceBuilder
import com.example.week_7_task.network.UploadEndPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


/**
 * The [ViewModel] that is attached to the [PokeliveFragment].
 */
class PokeliveViewModel : ViewModel() {

    private var url: String = ""
    private var prev = ""
    private var next = ""
    var limit = ""

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _message = MutableLiveData<String?>()

    // The external immutable LiveData for the request status String
    val message: LiveData<String?>
        get() = _message

    // Holds the results of the pokemons
    private val _property = MutableLiveData<List<Result>>()
    val property: LiveData<List<Result>>
        get() = _property


    // The internal MutableLiveData String that stores the most recent request
    private val _resultingPokemons = MutableLiveData<List<PokemonDetails>>()

    // The external immutable LiveData for the request of List of PokemonDetails
    val resultingPokemons: LiveData<List<PokemonDetails>>
        get() = _resultingPokemons


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * initNetworkRequest in the init block ensures that it is run
     * when the instance is first created
     */
    init {
        initNetworkRequest()
    }

    private fun initNetworkRequest() {
        coroutineScope.launch {
            // Calls the service builder to build to endpoint
            val request = ServiceBuilder.buildService(PokemonEndPoints::class.java)
            val getPokemonsDeferred = request.getPokemonAsync()

            try {
                val listResult = getPokemonsDeferred.await()
                if (listResult.results.isNotEmpty()) {
                    _property.value = listResult.results
                    prev = listResult.previous.toString()
                    next = listResult.next.toString()
                    /*
                      Creates a mutableList of resulting pokemon details, looped through all the array containing the List of
                      pokemon details and returns a list holding each pokemon and their details
                     */
                    val resultingPokemonDetails = mutableListOf<PokemonDetails>()
                    for (resultingPokemon in listResult.results) {

                        // Grabs the url of each pokemon and uses it to make a call to get particular pokemon and adds it to the list
                        url = resultingPokemon.url
                        val getResultingPokemonDeferred =
                            request.getParticularPokemonDetailsAsync(url)
                        val resultingPokemonDetail = getResultingPokemonDeferred.await()
                        resultingPokemonDetails.add(resultingPokemonDetail)
                    }
                    _resultingPokemons.value = resultingPokemonDetails

                }

            } catch (e: Exception) {
                _message.value = "Failure: ${e.message}"
            }

        }
    }

    /*
       Makes a call to get the previous set of items from the API
     */
    fun getPreviousPokemons() {
      getPokemons(prev)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /*
       Makes a call to get the next set of items from the API
     */
    fun getNextPokemons() {
       getPokemons(next)
    }

    /*
        Get a set number of pokemons
     */
    fun getLimitedPokemons(){
        getPokemons(limit)
    }

    fun getPokemons(length : String){
        coroutineScope.launch {
            _resultingPokemons.value = emptyList()
            val request = ServiceBuilder.buildService(PokemonEndPoints::class.java)
            val getPokemonsDeferred = request.getPreviousPokemonsAsync(length)


            try {
                val listResult = getPokemonsDeferred.await()
                if (listResult.results.isNotEmpty()) {
                    _property.value = listResult.results
                    prev = listResult.previous.toString()
                    next = listResult.next.toString()
                    Log.d("CHECKING", "Go In: ${listResult.results.size}")
                    val resultingPokemonDetails = mutableListOf<PokemonDetails>()
                    for (resultingPokemon in listResult.results) {
                        Log.d("CHECKING", "Go In: ${resultingPokemon.url}")
                        url = resultingPokemon.url
                        val getResultingPokemonDeferred =
                                request.getParticularPokemonDetailsAsync(url)
                        val resultingPokemonDetail = getResultingPokemonDeferred.await()
                        Log.d("CHECKING", "Go In: ${resultingPokemonDetail.name}")
                        resultingPokemonDetails.add(resultingPokemonDetail)
                    }
                    _resultingPokemons.value = resultingPokemonDetails
                }

            } catch (e: Exception) {
                _message.value = "Failure: ${e.message}"
            }
        }
    }
}

