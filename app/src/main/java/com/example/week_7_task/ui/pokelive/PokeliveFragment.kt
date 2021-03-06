package com.example.week_7_task.ui.pokelive

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.week_7_task.BASE_URL
import com.example.week_7_task.PokemonDetails
import com.example.week_7_task.R
import com.example.week_7_task.databinding.PokeliveFragmentBinding
import com.example.week_7_task.goto
import com.example.week_7_task.network.PokemonEndPoints

/**
 * This fragment holds the list of fragments being loaded into the recyclerview
 * Implements a clickListener to listen for click events and also initializes a viewmodel that
 * updates its ui when a livedata is received
 */
class PokeliveFragment : Fragment(), RecyclerViewClickListener {

    private lateinit var viewModel: PokeliveViewModel
    private var _binding: PokeliveFragmentBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PokeliveFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PokeliveViewModel::class.java)

        // Observes and passes the received list of pokemonDetails and passes into the adapter to populate the recycler view
        viewModel.resultingPokemons.observe(viewLifecycleOwner, Observer {
            val adapter = PokeliveAdapter(it)
            adapter.listener = this
            binding.pokemonRecyclerView.adapter = adapter
            if (it.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        })


        /**
         * Navigates to the second implementation
         */
        binding.navigation.setOnClickListener {
            goto(R.id.sendImage)
        }

        /**
         * Gets the no of pokemons user wants to see and gets the limited number
         */
        binding.setLimit.setOnClickListener {
            val number = binding.edtLimit.text.toString().toInt()
            val limit = BASE_URL + "pokemon?limit=$number&offset=0"
            viewModel.limit = limit
            viewModel.getLimitedPokemons()
        }

        /**
         * Onclicking if previous elements are available it returns the previous
         * 20 elements coming from the API
         */
        binding.btnPrev.setOnClickListener {
            viewModel.getPreviousPokemons()

        }

        /**
         * Onclicking if next elements are available it returns the next 20 elements from the API
         */
        binding.btnNext.setOnClickListener {
            viewModel.getNextPokemons()
        }
    }

    // Handles the click listener for each items in the pokeLiveFragment
    override fun onRecyclerViewItemClicked(view: View, pokemonDetails: PokemonDetails) {
        when (view.id) {
            R.id.pokeView -> {
                val action =
                    PokeliveFragmentDirections.actionPokeliveFragmentToPokeDetails(pokemonDetails.id)
                findNavController().navigate(action)
            }
        }
    }

}