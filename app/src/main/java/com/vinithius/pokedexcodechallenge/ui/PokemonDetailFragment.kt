package com.vinithius.pokedexcodechallenge.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.db.williamchart.view.HorizontalBarChartView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import com.vinithius.pokedexcodechallenge.R
import com.vinithius.pokedexcodechallenge.databinding.DamageViewholderBottomsheetBinding
import com.vinithius.pokedexcodechallenge.databinding.FragmentDetailPokemonBinding
import com.vinithius.pokedexcodechallenge.datasource.response.Damage
import com.vinithius.pokedexcodechallenge.datasource.response.DamageRelations
import com.vinithius.pokedexcodechallenge.datasource.response.Default
import com.vinithius.pokedexcodechallenge.datasource.response.Pokemon
import com.vinithius.pokedexcodechallenge.extension.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PokemonDetailFragment : Fragment() {

    private val viewModel by sharedViewModel<PokemonViewModel>()
    private lateinit var binding: FragmentDetailPokemonBinding
    private var toolbar: ActionBar? = null
    private var dominant: Palette.Swatch? = null
    private var dark: Palette.Swatch? = null


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
            val colorToolBar = ContextCompat.getColor(requireContext(), R.color.red_500)
            val colorWindow = ContextCompat.getColor(requireContext(), R.color.red_700)
            setBackgroundDrawable(ColorDrawable(colorToolBar))
            activity?.window?.statusBarColor = colorWindow
        }
        binding.layoutError.btnReloading.setOnClickListener {
            viewModel.getPokemonDetail()
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
        observerPokemonLoading()
        observerPokemonError()
        viewModel.getPokemonDetail()
    }

    private fun observerPokemonDetail() {
        with(viewModel) {
            pokemonDetail.observe(viewLifecycleOwner) { pokemon ->
                pokemon?.let {
                    toolbar?.apply { title = it.name.capitalize() }
                    setOutputs(it)
                }
            }
        }
    }

    /**
     * When request start, loading is visible, however, when finish, loading is invisible.
     */
    private fun observerPokemonLoading() {
        with(viewModel) {
            pokemonDetailLoading.observe(viewLifecycleOwner) { loading ->
                binding.loadingDetailPokemon.isGone = !loading
                binding.scrollDetailPokemon.isGone = loading
            }
        }
    }

    /**
     * If is error, main layout is hidden and show layout error.
     */
    private fun observerPokemonError() {
        with(viewModel) {
            pokemonDetailError.observe(viewLifecycleOwner) { error ->
                binding.layoutError.textError.isGone = !error
                binding.layoutError.btnReloading.isGone = !error
                binding.layoutError.pikachuSad.isGone = !error
                binding.scrollDetailPokemon.isGone = error
            }
        }
    }

    /**
     * Organize the sets.
     */
    private fun setOutputs(pokemon: Pokemon) {
        setDominantColor(pokemon)
        setPokemonImage(pokemon)
        setInfo(pokemon)
        setIsBaby(pokemon)
        setHabitat(pokemon)
        setEncounters(pokemon)
        setEggGroups(pokemon)
        setDamage(pokemon)
        setTextEntries(pokemon)
        setStats(pokemon)
        setTypes(pokemon)
        setAbilities(pokemon)
        setEvolutions(pokemon)
        setIconsMythicalAndLegendary(pokemon)
    }

    /**
     * Add information on the main card, such as photo, weight, height, type, etc.
     */
    private fun setInfo(pokemon: Pokemon) {
        with(binding.includeCardPokemonInfoAndImage) {
            val weightKl = String.format(ONE_DECIMAL, pokemon.weight?.converterIntToDouble())
            val weightLbs = String.format(ONE_DECIMAL, pokemon.weight?.convertPounds())
            textWeight.text = getString(R.string.kg_lbs, weightKl, weightLbs)
            val heightM = String.format(ONE_DECIMAL, pokemon.height?.converterIntToDouble())
            val heightInc = String.format(TWO_DECIMAL, pokemon.height?.convertInch())
            textHeight.text = getString(R.string.m_inch, heightM, heightInc)
            textCaptureRateValue.text = pokemon.specie?.capture_rate?.toString() ?: UNKNOWN
            textShapeValue.text = pokemon.specie?.shape?.name?.capitalize() ?: UNKNOWN
            textBaseValue.text = pokemon.base_experience.toString()
            pokemon.characteristic?.let { it ->
                val description = it.descriptions.filter { desc_filter ->
                    desc_filter.language.name == EN
                }.map { desc_map ->
                    desc_map.description
                }
                description.takeIf { value -> value.isNotEmpty() }.apply {
                    textDescription.text = description.first()
                }
            }
            textGenerationValue.text =
                pokemon.specie?.generation?.name?.split("-")?.last()?.uppercase() ?: UNKNOWN
        }
    }

    /**
     * Make sure it's a baby type.
     */
    private fun setIsBaby(pokemon: Pokemon) {
        pokemon.specie?.let {
            with(binding.includeCardIsABaby.cardViewIsABaby) {
                isVisible = it.is_baby
                setOnClickListener {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_baby),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Check the habitat and add colors from the palette.
     */
    private fun setHabitat(pokemon: Pokemon) {
        pokemon.specie?.let {
            with(binding.cardHabitat) {
                it.habitat?.name?.capitalize()?.let {
                    isVisible = true
                    setData(dark, dominant, listOf(it))
                }
            }
        }
    }

    /**
     * Check encounters and add palette colors.
     */
    private fun setEncounters(pokemon: Pokemon) {
        with(binding.cardEncounters) {
            val encounters = pokemon.encounters.map {
                it.location_area.name.replace("-", " ").capitalize()
            }
            setData(dark, dominant, encounters)
        }
    }

    /**
     * Check egg groups and add palette colors.
     */
    private fun setEggGroups(pokemon: Pokemon) {
        with(binding.cardEggGroups) {
            pokemon.specie?.let { specie ->
                val eggs = specie.egg_groups.map { it.name.capitalize() }
                isVisible = true
                setData(dark, dominant, eggs)
            }
        }
    }

    /**
     * Check the damage and populate the adapter.
     */
    private fun setDamage(pokemon: Pokemon) {
        with(binding.recyclerViewPokemonDamage) {
            isVisible = !pokemon.damage.isNullOrEmpty()
            isVisible.takeIf { it }.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = PokemonDamageAdapter(pokemon.damage).apply {
                    onCallBackClick = { damage ->
                        bottomSheetDamage(damage)
                    }
                }
            }
        }
    }

    /**
     * Check the description text and manipulate the format for HTML, such as BOLD and PARAGRAPH.
     */
    private fun setTextEntries(pokemon: Pokemon) {
        pokemon.specie?.let { specie ->
            with(binding) {
                textEntries.text = specie.flavor_text_entries.getHtmlCompat()
                constraintlayoutTextEntries.isVisible = true
            }
        }
    }

    /**
     * Set stats such ATTACK, DEFENSE and etc
     */
    private fun setStats(pokemon: Pokemon) {
        val myChart: HorizontalBarChartView = binding.myChart
        val mySet = mutableSetOf<Pair<String, Float>>()
        pokemon.stats?.forEach { stat ->
            mySet.add(
                Pair(
                    "${stat.stat.name.uppercase()} (${stat.base_stat})",
                    stat.base_stat.toFloat()
                )
            )
        }
        dominant?.let {
            myChart.barsColor = it.rgb
        }
        myChart.labelsFormatter = { it.toInt().toString() }
        myChart.show(mySet.toList())
    }

    /**
     * Set types such Grass, Poison and etc. Insert items into adapter too.
     */
    private fun setTypes(pokemon: Pokemon) {
        with(binding.includeCardPokemonInfoAndImage.recyclerViewPokemonType) {
            pokemon.types?.let { listType ->
                layoutManager = GridLayoutManager(context, 2)
                adapter = PokemonTypeAdapter(listType.map { it.type })
            }
        }
    }

    /**
     * Check abilities and add palette colors.
     */
    private fun setAbilities(pokemon: Pokemon) {
        with(binding.cardAbilities) {
            pokemon.abilities?.let { ability ->
                setData(
                    dark,
                    dominant,
                    ability.map { it.ability.name },
                    ability.map { it.is_hidden }
                )
            }
        }
    }

    /**
     * Check evolutions from getListEvolutions() and organize on screen with action detail.
     */
    private fun setEvolutions(pokemon: Pokemon) {
        with(binding.recyclerViewPokemonEvolutions) {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            pokemon.evolution?.let {
                isVisible = true
                adapter = PokemonEvolutionAdapter(it.getListEvolutions())
            }
        }
    }

    /**
     * If mystical or legendary, add mystical or legendary image.
     */
    private fun setIconsMythicalAndLegendary(pokemon: Pokemon) {
        pokemon.specie?.let {
            with(binding.includeCardPokemonInfoAndImage) {
                imageMythical.isVisible = it.is_mythical
                imageMythical.setOnClickListener {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_mythical),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                imageLegendary.isVisible = it.is_legendary
                imageLegendary.setOnClickListener {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_legendary),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Set pokemon's image in imageView.
     */
    private fun setPokemonImage(pokemon: Pokemon) {
        with(binding.includeCardPokemonInfoAndImage) {
            imagePokemon.setPokemonImage(pokemon, true)
            Picasso.get()
                .load(pokemon.sprites?.front_default)
                .into(imageSprite)
        }
    }

    /**
     * Set dominant color from pokemon's image in toolbar, window and background.
     */
    private fun setDominantColor(pokemon: Pokemon) {
        val dominantColor = pokemon.sprites?.front_default?.getDominantColorPallete()
        dominant = dominantColor?.get(COLOR_DOMINANT)
        dominantColor?.get(COLOR_LIGHT)?.let {
            binding.mainLayout.setBackgroundColor(ColorUtils.setAlphaComponent(it.rgb, ALPHA_100))
            activity?.window?.statusBarColor = ColorUtils.setAlphaComponent(it.rgb, MAX_ALPHA)
            val colorActionBar = ColorDrawable(ColorUtils.setAlphaComponent(it.rgb, ALPHA_220))
            toolbar?.setBackgroundDrawable(colorActionBar)
        }
    }

    private fun bottomSheetDamage(damage: Damage) {
        context?.let {
            val dialog = BottomSheetDialog(it)
            val bindingDamage =
                DamageViewholderBottomsheetBinding.inflate(LayoutInflater.from(requireContext()))
            setAdapters(bindingDamage, damage.damage_relations)
            bindingDamage.buttomClose.setOnClickListener {
                dialog.dismiss()
            }
            damage.type.name.setDrawableIco(it, bindingDamage.imageType)
            bindingDamage.titleDamage.text =
                it.getString(R.string.title_damage, damage.type.name.capitalize())
            dialog.setCancelable(true)
            dialog.setContentView(bindingDamage.root)
            dialog.show()
        }
    }

    /**
     * Set all damage adapters.
     */
    private fun setAdapters(
        bindingDamage: DamageViewholderBottomsheetBinding,
        damage: DamageRelations?
    ) {
        damage?.let {
            with(bindingDamage) {
                setAdapterDefault(
                    damage.effective_damage_from,
                    titleEffectiveDamageFrom,
                    recyclerEffectiveDamageFrom,
                    icoEffectiveDamageFrom
                )
                setAdapterDefault(
                    damage.effective_damage_to,
                    titleEffectiveDamageTo,
                    recyclerEffectiveDamageTo,
                    icoEffectiveDamageTo
                )
                setAdapterDefault(
                    damage.ineffective_damage_from,
                    titleIneffectiveDamageFrom,
                    recyclerIneffectiveDamageFrom,
                    icoIneffectiveDamageFrom
                )
                setAdapterDefault(
                    damage.ineffective_damage_to,
                    titleIneffectiveDamageTo,
                    recyclerIneffectiveDamageTo,
                    icoIneffectiveDamageTo
                )
                setAdapterDefault(
                    damage.no_damage_from,
                    titleNoDamageFrom,
                    recyclerNoDamageFrom,
                    icoNoDamageFrom
                )
                setAdapterDefault(
                    damage.no_damage_to,
                    titleNoDamageTo,
                    recyclerNoDamageTo,
                    icoNoDamageTo
                )
            }
        }
    }

    /**
     * Adapter default for all damage.
     */
    private fun setAdapterDefault(
        dataList: List<Default>,
        viewTitle: TextView,
        recycler: RecyclerView,
        ico: ImageView
    ) {
        if (dataList.isNullOrEmpty()) {
            viewTitle.visibility = View.GONE
            recycler.visibility = View.GONE
            ico.visibility = View.GONE
        } else {
            val layoutManager = object : GridLayoutManager(binding.root.context, COUNT_ITEMS) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            recycler.layoutManager = layoutManager
            recycler.adapter = PokemonTypeAdapter(dataList)
        }
    }

    companion object {
        const val ONE_DECIMAL = "%.1f"
        const val TWO_DECIMAL = "%.2f"
        const val EN = "en"
        const val UNKNOWN = "?"
        const val COLOR_LIGHT = "light"
        const val COLOR_DOMINANT = "dominant"
        const val ALPHA_100 = 100
        const val ALPHA_220 = 220
        const val MAX_ALPHA = 255
        const val COUNT_ITEMS = 4
    }
}
