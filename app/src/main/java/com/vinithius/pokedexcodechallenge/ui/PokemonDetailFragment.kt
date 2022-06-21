package com.vinithius.pokedexcodechallenge.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vinithius.pokedexcodechallenge.databinding.FragmentDetailPokemonBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PokemonDetailFragment : Fragment() {

    private val viewModel by sharedViewModel<PokemonViewModel>()
    private lateinit var binding: FragmentDetailPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentDetailPokemonBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerPokemonDetail()
        viewModel.getPokemonDetail()
    }

    private fun observerPokemonDetail() {
        with(viewModel) {
            pokemonDetail.observe(viewLifecycleOwner) { pokemon ->
                Log.i("observerPokemonDetail", pokemon.toString())
            }
        }
    }
}
