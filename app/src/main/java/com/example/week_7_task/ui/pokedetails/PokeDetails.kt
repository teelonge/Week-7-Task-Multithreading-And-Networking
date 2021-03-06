package com.example.week_7_task.ui.pokedetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.week_7_task.BASE_URL
import com.example.week_7_task.PokemonDetails
import com.example.week_7_task.R
import com.example.week_7_task.databinding.PokeDetailsFragmentBinding
import com.example.week_7_task.databinding.PokeliveFragmentBinding
import com.example.week_7_task.getPokemonColor

/**
 * This fragment holds individual pokemons and their specific
 * details
 */
class PokeDetails : Fragment() {

    private var _binding: PokeDetailsFragmentBinding? = null
    private val binding: PokeDetailsFragmentBinding
        get() = _binding!!

    private val args by navArgs<PokeDetailsArgs>()

    private lateinit var viewModel: PokeDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PokeDetailsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PokeDetailsViewModel::class.java)

        // retrieves the pokemon Id passed in from the all pokemon page and appends the Id to the url
        val pokemonId = args.pokemonId
        val url = "$BASE_URL/pokemon/$pokemonId"

        // The viewmodel sends this id and url to the pokemonDetail viewmodel to be used in there to fetch pokemon details
        viewModel.id = pokemonId
        viewModel.url = url

        // Initializes the network request to get each pokemon with all their details
        viewModel.initNetworkRequest()

        // Observes for changes in the value of the pokemon sprites array and sends new data to the adapter for processing
        viewModel.pokemonSprites.observe(viewLifecycleOwner, Observer {
            binding.detailsRecycler.adapter = PokeDetailsAdapter(it)
        })

        // gets the pokemon that was clicked and sets the image in the pokemon details using glide
        viewModel.resultingPokemon.observe(viewLifecycleOwner, Observer {
            val imageUri = it.sprites.front_default?.toUri()?.buildUpon()?.scheme("https")?.build()
            Glide.with(requireContext()).load(imageUri).apply(
                RequestOptions().placeholder(R.drawable.loading_status_animation)
                    .error(R.drawable.ic_error_image)
            ).into(binding.imgPokemon)

            /*
             Gets respective field in the pokemon details fragment and sets
             their values as derived from the received "it" pokemonDetails
             */
            fieldsBinder(it)

        })


    }

    /**
     * Takes in pokemonDetails as parameter and gets respective values from it
     */
    private fun fieldsBinder(it: PokemonDetails) {
        binding.txtPokeName.text = it.name
        binding.type1.text = it.types[0].type.name
        if (it.types.size > 1) {
            binding.type2.text = it.types[1].type.name
        }
        binding.txtPokeId.text = it.id.toString()

        binding.ability1.text = it.abilities[0].ability.name
        if (it.abilities.size > 1) {
            binding.ability3.text = it.abilities[1].ability.name
        }
        if (it.abilities.size > 2) {
            binding.ability3.text = it.abilities[2].ability.name
        }

        binding.txtHeight.text = it.height.toString()
        binding.txtWeight.text = it.weight.toString()

        binding.move1.text = it.moves[0].move.name
        binding.move2.text = it.moves[1].move.name
        binding.move3.text = it.moves[2].move.name

        binding.hpNo.text = it.stats[0].base_stat.toString()
        binding.attackNo.text = it.stats[1].base_stat.toString()
        binding.defenseNo.text = it.stats[2].base_stat.toString()
        binding.speedNo.text = it.stats[3].base_stat.toString()

        binding.move1.text = it.moves[0].move.name
        binding.move1.text = it.moves[1].move.name
        binding.move1.text = it.moves[2].move.name
        binding.move1.text = it.moves[3].move.name
        binding.move1.text = it.moves[4].move.name

        binding.pokeDetHolder.getPokemonColor(binding.root, it.types)
    }


}