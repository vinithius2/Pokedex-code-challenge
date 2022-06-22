package com.vinithius.pokedexcodechallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.vinithius.pokedexcodechallenge.R
import com.vinithius.pokedexcodechallenge.databinding.FragmentDetailPokemonBinding
import com.vinithius.pokedexcodechallenge.extension.capitalize
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PokemonDetailFragment : Fragment() {

    private val viewModel by sharedViewModel<PokemonViewModel>()
    private lateinit var binding: FragmentDetailPokemonBinding
    private var toolbar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar = (activity as MainActivity).supportActionBar
        toolbar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cleanPokemon()
        toolbar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = resources.getText(R.string.app_name)
        }
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
                pokemon?.let {
                    toolbar?.apply { title = it.name.capitalize() }
                }
            }
        }
    }
}
