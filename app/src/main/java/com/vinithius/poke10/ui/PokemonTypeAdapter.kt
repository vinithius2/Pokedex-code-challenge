package com.vinithius.poke10.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.R
import com.vinithius.poke10.databinding.TypeViewholderBinding
import com.vinithius.poke10.datasource.response.Default
import com.vinithius.poke10.extension.setDrawableIco

class PokemonTypeAdapter(
    private val pokemonTypeList: List<Default>
) : RecyclerView.Adapter<PokemonTypeAdapter.PokemonTypeViewHolder>() {

    private lateinit var view: View
    private lateinit var binding: TypeViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonTypeViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.type_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonTypeViewHolder, position: Int) {
        holder.bind(view.context, pokemonTypeList[position])
    }

    override fun getItemCount() = pokemonTypeList.size

    inner class PokemonTypeViewHolder(val binding: TypeViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, type: Default) {
            with(binding) {
                textType.text = type.name
                type.name.setDrawableIco(context, imageType)
            }
        }
    }
}
