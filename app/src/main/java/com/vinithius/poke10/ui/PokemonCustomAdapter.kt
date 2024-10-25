package com.vinithius.poke10.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.R
import com.vinithius.poke10.databinding.ShapeDefaultViewholderBinding
import com.vinithius.poke10.extension.capitalize
import com.vinithius.poke10.extension.setColorBackgroundGradientDrawable


class PokemonCustomAdapter(
    private val itemList: List<String>,
    private val dark: Palette.Swatch?,
    private val dominant: Palette.Swatch?,
    private val drawable: Drawable?,
    private val titleItemRight: String?,
    private val hiddenList: List<Boolean>,
) : RecyclerView.Adapter<PokemonCustomAdapter.PokemonCustomViewHolder>() {

    private lateinit var view: View
    private lateinit var binding: ShapeDefaultViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCustomViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shape_default_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonCustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonCustomViewHolder, position: Int) {
        val hidden: Boolean = if (hiddenList.isEmpty()) false else hiddenList[position]
        hiddenList.isNullOrEmpty()
        holder.bind(
            itemList[position],
            dominant,
            dark,
            drawable,
            titleItemRight,
            hidden
        )
    }

    override fun getItemCount() = itemList.size

    inner class PokemonCustomViewHolder(val binding: ShapeDefaultViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: String,
            dominant: Palette.Swatch?,
            dark: Palette.Swatch?,
            drawable: Drawable?,
            titleItemRight: String?,
            hidden: Boolean
        ) {
            with(binding) {
                textShape.text = item.capitalize()
                layoutShape.setColorBackgroundGradientDrawable(dominant)
                layoutShapeIco.setColorBackgroundGradientDrawable(dark)
                drawable?.let {
                    titleItemRight?.let { title ->
                        textShapeIcoText.text = title
                        textShapeIcoText.visibility = View.VISIBLE
                    }
                    imageShapeIcoText.background = it
                    layoutShapeIco.isGone = hidden
                }
            }
        }
    }
}
