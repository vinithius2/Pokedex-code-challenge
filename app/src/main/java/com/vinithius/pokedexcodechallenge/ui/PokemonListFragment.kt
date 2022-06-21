package com.vinithius.pokedexcodechallenge.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
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
        binding.layoutError.btnReloading.setOnClickListener {
            callAndObserverPokemons()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        callAndObserverPokemons()
    }


    private fun callAndObserverPokemons() {
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
            adapter.addLoadStateListener {
                loadStateErro(it)
                loadStateLoading(it)
            }
            adapter.addOnPagesUpdatedListener {
                progressBarPaging.isVisible = !progressBarPaging.isVisible
            }
            recyclerViewPokemons.adapter = adapter.apply {
                onCallBackClickDetail = { url ->
                    Log.i("DETAIL", url)
                }
                onCallBackClickFavorite = { favorite, pokemon ->
                    viewModel.setFavorite(pokemon, context)
                }
            }
        }
    }

    /**
     * Function to pass the 'error' state to the respective view, in this function it is possible to
     * enable the component's visibility.
     */
    private fun loadStateErro(state: CombinedLoadStates) {
        with(binding) {
            val error = state.refresh is LoadState.Error
            layoutError.btnReloading.isVisible = error
            layoutError.pikachuSad.isVisible = error
            layoutError.textError.isVisible = error
            recyclerViewPokemons.isVisible = !error
        }
    }

    /**
     * Function to pass the 'loading' state to the respective view, in this function it is possible
     * to enable the component's visibility.
     */
    private fun loadStateLoading(state: CombinedLoadStates) {
        with(binding) {
            val visible = state.refresh is LoadState.Loading && adapter.itemCount == 0
            loadingListPokemon.isVisible = visible
            recyclerViewPokemons.isVisible = !visible
        }
    }

}