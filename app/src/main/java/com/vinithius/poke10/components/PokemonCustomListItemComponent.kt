package com.vinithius.poke10.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vinithius.poke10.R
import com.vinithius.poke10.databinding.PokemonCustomListItemBottomsheetBinding
import com.vinithius.poke10.databinding.PokemonCustomListItemComponentBinding
import com.vinithius.poke10.ui.PokemonCustomAdapter


class PokemonCustomListItemComponent(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private var darkCard: Palette.Swatch? = null
    private var dominantCard: Palette.Swatch? = null
    private var drawableIco: Drawable? = null

    private var expand_card: Boolean = false
        set(value) {
            field = value
            setVisible(value)
        }

    private var titleItemRight: String? = null
    private var titleCard: String? = null
        set(value) = setTitle(value)

    private lateinit var hiddenListCard: List<Boolean>
    private var dataListCard: List<String> = listOf()
        set(value) {
            field = value
            setAdapter(value)
        }

    private val binding =
        PokemonCustomListItemComponentBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        attrs?.let { attributeSet ->
            val attribute = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.PokemonCustomListItemComponent
            )
            drawableIco =
                attribute.getDrawable(R.styleable.PokemonCustomListItemComponent_ico_card)
            titleCard =
                attribute.getString(R.styleable.PokemonCustomListItemComponent_title_card)
            titleItemRight =
                attribute.getString(R.styleable.PokemonCustomListItemComponent_title_item_right)
            expand_card =
                attribute.getBoolean(R.styleable.PokemonCustomListItemComponent_expand_card, false)
            attribute.recycle()
        }
    }

    /**
     * Put icon in left position.
     */
    private fun setIco(title: String) {
        getIco(title)?.let {
            binding.icoLeft.setImageResource(it)
        }
    }

    /**
     * Get icon by name.
     */
    private fun getIco(title: String): Int? {
        return when (title.lowercase()) {
            ABILITIES -> R.drawable.ic_left_abilities
            EGG_GROUPS -> R.drawable.ic_left_egg_groups
            HABITAT -> R.drawable.ic_left_habitat
            ENCOUNTERS -> R.drawable.ic_left_encounters
            else -> null
        }
    }

    /**
     * Set visibilitie in views.
     */
    private fun setVisible(value: Boolean) {
        if (!value) {
            with(binding) {
                recyclerViewPokemonCustom.isGone = true
                cardviewCustom.setOnClickListener {
                    bottomSheetDamage()
                }
            }
        }
    }

    /**
     * Set title in component.
     */
    private fun setTitle(value: String?) {
        value?.let {
            binding.titleCustom.text = it
            setIco(it)
        }
    }

    /**
     * Set list items in adapter.
     */
    private fun setAdapter(dataList: List<String>) {
        with(binding) {
            if (dataList.isNullOrEmpty()) {
                cardviewCustom.visibility = View.GONE
            } else {
                val layoutManager = LinearLayoutManager(context)
                recyclerViewPokemonCustom.layoutManager = layoutManager
                recyclerViewPokemonCustom.adapter =
                    PokemonCustomAdapter(
                        dataList,
                        darkCard,
                        dominantCard,
                        drawableIco,
                        titleItemRight,
                        hiddenListCard
                    )
            }
        }
    }

    /**
     * Set bottomsheet items.
     */
    private fun bottomSheetDamage() {
        context?.let {
            val dialog = BottomSheetDialog(it)
            dialog.setCancelable(true)
            val bindingCustom =
                PokemonCustomListItemBottomsheetBinding.inflate(LayoutInflater.from(context))
            with(bindingCustom) {
                val layoutManager = LinearLayoutManager(context)
                recyclerViewPokemonCustomBottomsheet.layoutManager = layoutManager
                recyclerViewPokemonCustomBottomsheet.adapter =
                    PokemonCustomAdapter(
                        dataListCard,
                        darkCard,
                        dominantCard,
                        drawableIco,
                        titleItemRight,
                        hiddenListCard
                    )
                buttomClose.setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.setContentView(bindingCustom.root)
            dialog.show()
        }
    }

    fun setData(
        dark: Palette.Swatch?,
        dominant: Palette.Swatch?,
        dataList: List<String>,
        hiddenList: List<Boolean> = listOf()
    ) {
        darkCard = dark
        dominantCard = dominant
        hiddenListCard = hiddenList
        dataListCard = dataList
    }

    companion object {
        const val ABILITIES = "abilities"
        const val EGG_GROUPS = "egg groups"
        const val HABITAT = "habitat"
        const val ENCOUNTERS = "encounters"
    }

}
