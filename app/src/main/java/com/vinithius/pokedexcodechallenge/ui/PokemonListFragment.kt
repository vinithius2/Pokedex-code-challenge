package com.vinithius.pokedexcodechallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinithius.pokedexcodechallenge.databinding.FragmentListPokemonBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PokemonListFragment : Fragment() {

    private val viewModel by sharedViewModel<PokemonViewModel>()
    private lateinit var binding: FragmentListPokemonBinding
    private lateinit var adapter: PokemonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentListPokemonBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        observerPokemons()
    }


    private fun observerPokemons(nameStartsWith: String? = null) {
        with(viewModel) {
            getPokemonList()?.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setAdapter() {
        val linearLayout = LinearLayoutManager(activity)
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        with(binding) {
            recyclerViewPokemons.layoutManager = linearLayout
            recyclerViewPokemons.setHasFixedSize(true)
            adapter = PokemonListAdapter()
            adapter.addOnPagesUpdatedListener {
                progressBarPaging.isVisible = !progressBarPaging.isVisible
            }
        }
    }

}