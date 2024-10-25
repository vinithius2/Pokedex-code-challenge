package com.vinithius.poke10.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.R
import com.vinithius.poke10.databinding.DamageViewholderBinding
import com.vinithius.poke10.datasource.response.Damage
import com.vinithius.poke10.extension.capitalize
import com.vinithius.poke10.extension.setDrawableIco


class PokemonDamageAdapter(
    private val pokemonDamageList: List<Damage>
) : RecyclerView.Adapter<PokemonDamageAdapter.PokemonDamageViewHolder>() {

    private lateinit var view: View
    private lateinit var binding: DamageViewholderBinding
    var onCallBackClick: ((damage: Damage) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonDamageViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.damage_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonDamageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonDamageViewHolder, position: Int) {
        holder.bind(view.context, pokemonDamageList[position])
    }

    override fun getItemCount() = pokemonDamageList.size

    inner class PokemonDamageViewHolder(val binding: DamageViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, damage: Damage) {
            with(binding) {
                setIco(context, damage, imageType)
                setTitle(damage, titleDamage)
                root.setOnClickListener {
                    onCallBackClick?.invoke(damage)
                }
            }
        }

        /**
         * Add drawable damage left ico in ImageView.
         */
        private fun setIco(context: Context, damage: Damage, imageType: ImageView) {
            damage.type.name.setDrawableIco(context, imageType)
        }

        /**
         * Add text damage.
         */
        private fun setTitle(damage: Damage, textView: TextView) {
            textView.text =
                binding.root.context.getString(R.string.title_damage, damage.type.name.capitalize())
        }

    }

}
